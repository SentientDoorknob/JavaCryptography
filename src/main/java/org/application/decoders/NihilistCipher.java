package org.application.decoders;

import org.crypography_tools.Tools;

import java.util.ArrayList;
import java.util.Arrays;

public class NihilistCipher {

    public static boolean[] EvaluateUnits(int[] coset) {
        boolean[] hasEnding = new boolean[10];

        for (int i : coset) {
            int units = i % 10;
            int index = Math.floorMod(units - 1, 10);

            hasEnding[index] = true;
        }

        return hasEnding;
    }

    public static boolean[] EvaluateTens(int[] coset) {
        boolean[] hasEnding = new boolean[10];

        for (int i : coset) {
            int units = (i % 100 - i % 10) / 10;
            int index = Math.floorMod(units - 1, 10);

            hasEnding[index] = true;
        }

        return hasEnding;
    }

    public static int[] GetSpans(boolean[][] bools) {
        int length = bools.length;
        int[] spans = new int[length];

        for (int i = 0; i < length; i++) {
            int[] trues = Tools.GetTrues(bools[i]).stream().mapToInt(Integer::intValue).toArray();
            int maxIndex = Arrays.stream(trues).max().orElse(72);
            int minIndex = Arrays.stream(trues).min().orElse(0);

            spans[i] = maxIndex - minIndex + 1;
        }

        return spans;
    }

    public static int[][] GetDigits(boolean[][] bools) {
        int keywordLength = bools.length;
        int[][] lastDigits = new int[keywordLength][5];

        for (int[] digitOption : lastDigits) {
            Arrays.fill(digitOption, -1);
        }

        for (int i = 0; i < keywordLength; i++) {
            int[] span = Tools.GetMaximumSpan(bools[i]);
            // span, starting value

            if (span[0] >= 5) {
                lastDigits[i][0] = span[1];
            }
            else {
                int numPossibilities = 5 - span[0];

                for (int j = 0; j < numPossibilities; j++) {
                    lastDigits[i][j] = Math.floorMod(span[0] - j, 10);
                }
            }
        }

        return lastDigits;
    }

    public static boolean[][] GetCosetTableIter(String ciphertext, int keywordLength, boolean evaluate10s) {

        int[] ints = Arrays.stream(ciphertext.split(" ")).mapToInt(Integer::parseInt).toArray();
        int[][] cosets = Tools.MakeIntArrayCosets(ints, keywordLength);

        boolean[][] results = new boolean[keywordLength][10];

        for (int i = 0; i < keywordLength; i++) {
            results[i] = evaluate10s? EvaluateTens(cosets[i]) : EvaluateUnits(cosets[i]);
        }

        int[] spans = GetSpans(results);
        boolean isValid = Arrays.stream(spans).allMatch(x -> x <= 5);

        if (!isValid) {
            return GetCosetTableIter(ciphertext, keywordLength + 1, evaluate10s);
        }

        return results;
    }

    public static boolean[][] GetCosetTableFixed(String ciphertext, int keywordLength, boolean evaluate10s) {
        int[] ints = Arrays.stream(ciphertext.split(" ")).mapToInt(Integer::parseInt).toArray();
        int[][] cosets = Tools.MakeIntArrayCosets(ints, keywordLength);

        boolean[][] results = new boolean[keywordLength][10];

        for (int i = 0; i < keywordLength; i++) {
            results[i] = evaluate10s? EvaluateTens(cosets[i]) : EvaluateUnits(cosets[i]);
        }

        int[] spans = GetSpans(results);
        boolean isValid = Arrays.stream(spans).allMatch(x -> x <= 5);

        return results;
    }

    public static int[][] TrimResults(int[][] input, int value) {
        ArrayList<int[]> filteredList = new ArrayList<>();

        for (int[] row : input) {
            int[] newRow = Arrays.stream(row).filter(x -> x != value).toArray();
            filteredList.add(newRow);
        }

        return filteredList.toArray(new int[0][]);
    }

    public static int[][][] GetDigitPossibilities(String ciphertext) {
        boolean[][] results = GetCosetTableIter(ciphertext, 1, false);
        int[][] lastDigits = TrimResults(GetDigits(results), -1);

        int keywordLength = results.length;

        boolean[][] resultsTens = GetCosetTableFixed(ciphertext, keywordLength, true);
        int[][] firstDigits = TrimResults(GetDigits(resultsTens), -1);

        System.out.println(Arrays.deepToString(lastDigits));
        System.out.println(Arrays.deepToString(firstDigits));
        System.out.println();

        return new int[][][] {firstDigits, lastDigits};
    }

    public static int[][] GetKeywordPossibilities(int[][][] digitPossibilities) {

    }

    public static void ConvertWithResultsDialogue(String ciphertext) {
        int[][][] DigitPossibilities = GetDigitPossibilities(ciphertext);


    }
}

//  55 14 23 45 51 55 44