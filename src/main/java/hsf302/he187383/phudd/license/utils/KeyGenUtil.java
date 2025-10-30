package hsf302.he187383.phudd.license.utils;

import java.io.StringWriter;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class KeyGenUtil {

    /**
     * Sinh cặp RSA 2048-bit, trả về dạng PEM (PKCS#8 / X.509)
     */
    public static PemPair generateRsaPemPair(int keySize) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(keySize);
            KeyPair kp = kpg.generateKeyPair();

            String privatePem = encodePrivateKeyToPem(kp.getPrivate());
            String publicPem = encodePublicKeyToPem(kp.getPublic());

            return new PemPair(privatePem, publicPem);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate RSA key pair", e);
        }
    }

    public static PemPair generateRsaPemPair() {
        return generateRsaPemPair(2048);
    }

    // ==== Encode helpers ====

    private static String encodePrivateKeyToPem(PrivateKey privateKey) {
        byte[] encoded = privateKey.getEncoded(); // PKCS#8
        String base64 = Base64.getEncoder().encodeToString(encoded);
        return wrapPem(base64, "PRIVATE KEY");
    }

    private static String encodePublicKeyToPem(PublicKey publicKey) {
        byte[] encoded = publicKey.getEncoded(); // X.509
        String base64 = Base64.getEncoder().encodeToString(encoded);
        return wrapPem(base64, "PUBLIC KEY");
    }

    private static String wrapPem(String base64, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append("-----BEGIN ").append(type).append("-----\n");
        // Chia dòng 64 ký tự theo chuẩn PEM
        for (int i = 0; i < base64.length(); i += 64) {
            sb.append(base64, i, Math.min(i + 64, base64.length())).append("\n");
        }
        sb.append("-----END ").append(type).append("-----\n");
        return sb.toString();
    }

    // Record tiện lợi để chứa 2 chuỗi PEM
    public record PemPair(String privatePem, String publicPem) {
        @Override
        public String toString() {
            return "Private:\n" + privatePem + "\nPublic:\n" + publicPem;
        }
    }
}
