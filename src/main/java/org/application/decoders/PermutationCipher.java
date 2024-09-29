package org.application.decoders;

import org.crypography_tools.Digram;
import org.crypography_tools.Tools;

public class PermutationCipher {
    public static double evaluateColumnPair(String c1, String c2) {
        if (c1.equals(c2)) {
            return -10;
        }

        int columnSize = Math.min(c1.length(), c2.length());

        int sum = 0;
        for (int i = 0; i < columnSize; i++) {
            int l = c1.charAt(i) - 'a';
            int m = c2.charAt(i) - 'a';

            sum += Digram.permutationScores[l][m];
        }

        return (float) sum / columnSize;
    }

    public static double[][] evaluateCiphertext(String ciphertext, int length) {

        if ( length > 30) {
            return null;
        }

        String[] cosets = Tools.MakeCosets(ciphertext, length);

        double[][] results = new double[length][length];

        int noPositives = 0;

        for (int i = 0; i < length; i++) {
            String c1 = cosets[i];

            for (int j = 0; j < length; j++) {
                String c2 = cosets[j];

                double columnEvaluation = evaluateColumnPair(c1, c2);
                results[i][j] = columnEvaluation;

                if (columnEvaluation > 0) {
                    noPositives++;
                }
            }
        }

        if (noPositives >= length - 2) {
            return results;
        }

        return evaluateCiphertext(ciphertext, length + 1);
    }
}
