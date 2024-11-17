package org.application.decoders;

import org.application.Main;
import org.application.results.cipher.PermutationResult;
import org.crypography_tools.Digram;
import org.crypography_tools.Tools;

import java.util.Arrays;
import java.util.stream.Collectors;

/*

    Method Source: https://homepages.math.uic.edu/~leon/mcs425-s08/handouts/breaking_tranposition_cipher.pdf

    PAIRS -> How likely 2 Columns are to be next to each other.
    KEY -> Array of integers, ascending order.

 */

public class PermutationCipher {
    public static final int MAX_KEYWORD_LENGTH = 30;

    // CIPHERTEXT -> PAIRS -> KEY -> PLAINTEXT

    // UTIL -> EvaluateCiphertext
    public static double EvaluateColumnPair(String c1, String c2) {
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

    // Returns PAIRS. Recursively follows method with incrementing keyword lengths.
    public static double[][] EvaluateCiphertext(String ciphertext, int length) {

        if ( length > MAX_KEYWORD_LENGTH) {
            return null;
        }

        String[] cosets = Tools.MakeCosets(ciphertext, length);

        double[][] results = new double[length][length];

        int noPositives = 0;

        for (int i = 0; i < length; i++) {
            String c1 = cosets[i];

            for (int j = 0; j < length; j++) {
                String c2 = cosets[j];

                double columnEvaluation = EvaluateColumnPair(c1, c2);
                results[i][j] = columnEvaluation;

                if (columnEvaluation > 0) {
                    noPositives++;
                }
            }
        }

        if (noPositives >= length - 2) {
            return results;
        }

        return EvaluateCiphertext(ciphertext, length + 1);
    }

    // Returns KEY. See method - follows path of adjacent columns.
    public static int[] GetKeyword(double[][] pairs) {
        int emptyColumn = 1;
        int keywordLength = pairs.length;

        for (int i = 0; i < keywordLength; i++) {
            boolean isNegative = Arrays.stream(Tools.GetColumn(pairs, i)).allMatch(x -> x < 0);
            if (isNegative) {emptyColumn = i;}
        }

        int[] keyword = new int[keywordLength];

        for (int i = 0; i < keywordLength; i++) {
            keyword[i] = emptyColumn;

            double maxValue = Arrays.stream(pairs[emptyColumn]).max().orElseThrow();
            emptyColumn = Arrays.stream(pairs[emptyColumn]).boxed().collect(Collectors.toList()).indexOf(maxValue);
        }

        return keyword;
    }

    // Returns PLAINTEXT. Rearranges columns.
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
        double[][] pairs = EvaluateCiphertext(ciphertext, 4);
        assert pairs != null;
        int[] keyword = GetKeyword(pairs);
        String plaintext = DecryptWithKeyword(ciphertext, keyword);

        PermutationResult result = new PermutationResult(ciphertext, plaintext, keyword, PermutationCipher::DecryptFromResultsDialogue);

        Main.boxHandler.OpenPermutationOutput(result);
    }

    public static void DecryptFromResultsDialogue(PermutationResult result) {
        result.usedKeyword = Tools.ReducePermutationKeyword(Arrays.stream(result.usedKeyword).toArray());
        result.plaintext = DecryptWithKeyword(result.ciphertext, result.usedKeyword);

        Main.boxHandler.OpenPermutationOutput(result);
    }

}
