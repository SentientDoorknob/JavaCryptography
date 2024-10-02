package org.application.decoders;

import org.application.Main;
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

    public static int[] getKeyword(String ciphertext) {
        double[][] results = evaluateCiphertext(ciphertext, 3);
        return AnalyseResults(results);
    }

    public static int[] AnalyseResults(double[][] results) {
        int emptyColumn = 1;
        int keywordLength = results.length;

        for (int i = 0; i < keywordLength; i++) {
            boolean isNegative = Arrays.stream(Tools.GetColumn(results, i)).allMatch(x -> x < 0);
            if (isNegative) {emptyColumn = i;}
        }

        int[] keyword = new int[keywordLength];

        for (int i = 0; i < keywordLength; i++) {
            keyword[i] = emptyColumn;

            double maxValue = Arrays.stream(results[emptyColumn]).max().orElseThrow();
            emptyColumn = Arrays.stream(results[emptyColumn]).boxed().collect(Collectors.toList()).indexOf(maxValue);
        }

        return keyword;
    }

    public static String DecryptWithKeyword(String ciphertext, int[] keyword) {
        int keywordLength = keyword.length;
        String[] cosets = Tools.MakeCosets(ciphertext, keywordLength);

        String[] permutedCosets = new String[keywordLength];

        for (int i = 0; i < keywordLength; i++) {
            permutedCosets[i] = cosets[keyword[i]];
        }

        return Tools.Interleave(permutedCosets);
    }

    public static void DecryptWithResultsDialogue(String ciphertext) {
        int[] keyword = getKeyword(ciphertext);
        String plaintext = DecryptWithKeyword(ciphertext, keyword);

        Main.boxHandler.OpenPermutationOutput(keyword, keyword, plaintext, ciphertext);
    }

    public static void DecryptFromResultsDialogue(String ciphertext, String[] keyword, int[] predictedKeyword) {
        System.out.println(Arrays.toString(keyword));
        int[] reducedKeyword = Tools.ReducePermutationKeyword(Arrays.stream(keyword).mapToInt(x -> Integer.parseInt(x)).toArray());
        System.out.println(Arrays.toString(reducedKeyword));
        String plaintext = DecryptWithKeyword(ciphertext, reducedKeyword);

        Main.boxHandler.OpenPermutationOutput(predictedKeyword, reducedKeyword, plaintext, ciphertext);
    }

}
