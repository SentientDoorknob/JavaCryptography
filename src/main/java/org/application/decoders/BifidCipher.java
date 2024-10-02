package org.application.decoders;

import org.crypography_tools.Tools;

public class BifidCipher {
    public static String ToSubstitutionCipher(String ciphertext) {
        int halfLength = (int) Math.floor((double) ciphertext.length() / 2);

        System.out.println(ciphertext);

        int[] firstHalf = ciphertext.substring(0, halfLength).chars().map(x -> x - '0' - 1).toArray();
        int[] secondHalf = ciphertext.substring(halfLength).chars().map(x -> x - '0' - 1).toArray();

        String substitution = "";

        for (int i = 0; i < halfLength; i++) {
            substitution += Tools.Bifid[firstHalf[i]][secondHalf[i]];
        }

        System.out.println(Tools.IndexOfCoincidence(substitution));

        return substitution;
    }

    public static String ToSubustitutionCipher2(String ciphertext) {
        int[] ints = ciphertext.chars().map(x -> x - '0' - 1).toArray();

        String substitution = "";

        for (int i = 0; i < ciphertext.length(); i += 2) {
            substitution += Tools.Bifid[ints[i]][ints[i+1]];
        }

        System.out.println(Tools.IndexOfCoincidence(substitution));

        return substitution;
    }

}
