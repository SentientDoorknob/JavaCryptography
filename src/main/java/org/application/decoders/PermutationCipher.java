package org.application.decoders;

import org.crypography_tools.Digram;
import org.crypography_tools.Tools;

import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

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

    public static String getKeyword(String ciphertext) {
        double[][] results = evaluateCiphertext(ciphertext, 3);
        return AnalyseResults(results);
    }

    public static String getKeywordFromLength(String ciphertext, int length) {
        double[][] results = evaluateCiphertext(ciphertext, length);
        return AnalyseResults(results);
    }

    public static String AnalyseResults(double[][] results) {
        int emptyColumn = 1;

        for (int i = 0; i < results.length; i++) {
            boolean isNegative = Arrays.stream(Tools.getColumn(results, i)).allMatch(x -> x < 0);
            if (isNegative) {emptyColumn = i;}
        }

        String keyword = "";

        for (int i = 0; i < results.length; i++) {
            keyword += emptyColumn;

            double maxValue = Arrays.stream(results[emptyColumn]).max().orElseThrow();
            emptyColumn = Arrays.stream(results[emptyColumn]).boxed().collect(Collectors.toList()).indexOf(maxValue);
        }

        return keyword;
    }

    public static String DecryptWithKeyword(String ciphertext, String keyword) {
        int keywordLength = keyword.length();
        String[] cosets = Tools.MakeCosets(ciphertext, keywordLength);

        String[] permutedCosets = new String[keywordLength];

        for (int i = 0; i < keywordLength; i++) {
            permutedCosets[i] = cosets[keyword.charAt(i) - '0'];
        }

        return Tools.Interleave(permutedCosets);
    }

    public static void DecryptWithResultsDialogue(String ciphertext) {
        String keyword = getKeyword(ciphertext);
        System.out.println(keyword);

        String plaintext = DecryptWithKeyword(ciphertext, keyword);
        System.out.println(plaintext);
    }

}
