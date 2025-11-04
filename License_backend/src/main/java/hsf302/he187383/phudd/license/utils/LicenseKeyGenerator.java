package hsf302.he187383.phudd.license.utils;

import java.security.SecureRandom;

public final class LicenseKeyGenerator {
    private static final char[] ALPH = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray(); // no 0/1/O/I
    private static final SecureRandom RND = new SecureRandom();

    private LicenseKeyGenerator(){}

    public static String randomKey(String planCode) {
        String prefix = planCode == null ? "PLAN" : planCode.replaceAll("[^A-Z0-9]", "").toUpperCase();
        String body = randomBlocks(4, 4); // 4 blocks x 4 = 16 chars
        return prefix + "-" + body;
    }

    private static String randomBlocks(int blocks, int len) {
        StringBuilder sb = new StringBuilder();
        for (int b = 0; b < blocks; b++) {
            if (b > 0) sb.append("-");
            for (int i = 0; i < len; i++) sb.append(ALPH[RND.nextInt(ALPH.length)]);
        }
        return sb.toString();
    }
}
