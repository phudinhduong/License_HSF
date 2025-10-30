package hsf302.he187383.phudd.license.utils;

import java.io.BufferedReader;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import java.util.Base64;

public final class PemUtils {
    private PemUtils() {}

    // ==== PRIVATE KEY ====
    public static PrivateKey readPrivateKeyFromPem(String pem) {
        try {
            String type = firstPemHeaderLine(pem);
            if (type.contains("ENCRYPTED PRIVATE KEY")) {
                throw new IllegalArgumentException(
                        "Encrypted PKCS#8 private key detected. Provide password and decrypt first " +
                                "(e.g. openssl pkcs8 -in enc.key -passin pass:xxx -out plain.key)"
                );
            }
            if (type.contains("EC PRIVATE KEY")) {
                throw new IllegalArgumentException("EC private key provided, but RS256 requires RSA key pair.");
            }

            if (type.contains("RSA PRIVATE KEY")) {
                // PKCS#1 → wrap thành PKCS#8
                byte[] pkcs1 = base64Body(pem);
                byte[] pkcs8 = wrapPkcs1ToPkcs8(pkcs1);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                return kf.generatePrivate(new PKCS8EncodedKeySpec(pkcs8));
            }

            if (type.contains("PRIVATE KEY")) {
                // PKCS#8 unencrypted
                byte[] pkcs8 = base64Body(pem);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                return kf.generatePrivate(new PKCS8EncodedKeySpec(pkcs8));
            }

            throw new IllegalArgumentException("Unsupported private key PEM header: " + type);
        } catch (Exception ex) {
            throw new RuntimeException("Invalid private key PEM", ex);
        }
    }

    // ==== PUBLIC KEY ====
    public static PublicKey readPublicKeyFromPem(String pem) {
        try {
            String type = firstPemHeaderLine(pem);
            if (type.contains("RSA PUBLIC KEY")) {
                // PKCS#1 Public → wrap sang SPKI (X.509)
                byte[] pkcs1 = base64Body(pem);
                byte[] spki = wrapRsaPkcs1PublicToSpki(pkcs1);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                return kf.generatePublic(new X509EncodedKeySpec(spki));
            }
            if (type.contains("PUBLIC KEY")) {
                // SPKI/X.509
                byte[] spki = base64Body(pem);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                return kf.generatePublic(new X509EncodedKeySpec(spki));
            }
            throw new IllegalArgumentException("Unsupported public key PEM header: " + type);
        } catch (Exception ex) {
            throw new RuntimeException("Invalid public key PEM", ex);
        }
    }

    // ==== Helpers ====

    private static String firstPemHeaderLine(String pem) throws Exception {
        try (BufferedReader br = new BufferedReader(new StringReader(pem))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("-----BEGIN ")) return line;
            }
        }
        return "";
    }

    private static byte[] base64Body(String pem) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new StringReader(pem))) {
            String line; boolean body = false;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("-----BEGIN ")) { body = true; continue; }
                if (line.startsWith("-----END ")) break;
                if (body && !line.isEmpty()) sb.append(line);
            }
        }
        return Base64.getDecoder().decode(sb.toString());
    }

    /**
     * Wrap PKCS#1 (RSAPrivateKey) → PKCS#8 (PrivateKeyInfo)
     * PKCS#8 structure: SEQUENCE { version, AlgorithmIdentifier(rsaEncryption), OCTET STRING(pkcs1) }
     */
    private static byte[] wrapPkcs1ToPkcs8(byte[] pkcs1) {
        // ASN.1 DER building with minimal dependencies
        // AlgorithmIdentifier for rsaEncryption: 1.2.840.113549.1.1.1 + NULL
        byte[] algId = new byte[] {
                0x30, 0x0D,                         // SEQUENCE (13)
                0x06, 0x09,                         // OBJECT IDENTIFIER (9)
                0x2A, (byte)0x86, 0x48, (byte)0x86, (byte)0xF7, 0x0D, 0x01, 0x01, 0x01,
                0x05, 0x00                          // NULL
        };

        byte[] pkcs1OctetString = derOctetString(pkcs1);
        byte[] version = new byte[] { 0x02, 0x01, 0x00 }; // INTEGER 0

        byte[] seqContent = concat(version, algId, pkcs1OctetString);
        return derSequence(seqContent);
    }

    /**
     * Wrap RSA PUBLIC KEY (PKCS#1) → SubjectPublicKeyInfo (X.509)
     * SPKI: SEQUENCE{ AlgorithmIdentifier, BIT STRING (pubkey pkcs1) }
     */
    private static byte[] wrapRsaPkcs1PublicToSpki(byte[] pkcs1Pub) {
        byte[] algId = new byte[] {
                0x30, 0x0D,
                0x06, 0x09,
                0x2A, (byte)0x86, 0x48, (byte)0x86, (byte)0xF7, 0x0D, 0x01, 0x01, 0x01,
                0x05, 0x00
        };
        // BIT STRING: 0x03 len 0x00 + bytes
        byte[] bitString = derBitString(pkcs1Pub);
        byte[] seqContent = concat(algId, bitString);
        return derSequence(seqContent);
    }

    // DER helpers
    private static byte[] derSequence(byte[] content) {
        return concat(new byte[]{0x30}, derLength(content.length), content);
    }
    private static byte[] derOctetString(byte[] content) {
        return concat(new byte[]{0x04}, derLength(content.length), content);
    }
    private static byte[] derBitString(byte[] content) {
        byte[] withUnusedBits = concat(new byte[]{0x00}, content); // 0 unused bits
        return concat(new byte[]{0x03}, derLength(withUnusedBits.length), withUnusedBits);
    }
    private static byte[] derLength(int len) {
        if (len < 128) return new byte[]{(byte) len};
        if (len < 256) return new byte[]{(byte)0x81, (byte)len};
        return new byte[]{(byte)0x82, (byte)(len >> 8), (byte)len};
    }
    private static byte[] concat(byte[]... arrs) {
        int len = 0; for (byte[] a : arrs) len += a.length;
        byte[] out = new byte[len]; int p = 0;
        for (byte[] a : arrs) { System.arraycopy(a, 0, out, p, a.length); p += a.length; }
        return out;
    }
}
