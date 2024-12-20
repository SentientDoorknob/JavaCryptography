package org.application.decoders;

import org.application.DialogueBoxHandler;
import org.application.Main;
import org.crypography_tools.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class NihilistCipher {

    // Okay so im like not doing this

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

    public static int[][] GetDigitPossibilities(String ciphertext) {
        boolean[][] results = GetCosetTableIter(ciphertext, 1, false);
        int[][] lastDigits = TrimResults(GetDigits(results), -1);

        int keywordLength = results.length;

        boolean[][] resultsTens = GetCosetTableFixed(ciphertext, keywordLength, true);
        int[][] firstDigits = TrimResults(GetDigits(resultsTens), -1);

        System.out.println(Arrays.deepToString(lastDigits));
        System.out.println(Arrays.deepToString(firstDigits));
        System.out.println();

        return Stream.concat(Arrays.stream(firstDigits), Arrays.stream(lastDigits))
                .toArray(int[][]::new);
    }

    public static int[][] GetKeywordPossibilities(int[][] digitPossibilities) {
        ArrayList<ArrayList<Integer>> last_iteration = new ArrayList<>();
        last_iteration.add(new ArrayList<>());

        for (int[] digit : digitPossibilities) {
            ArrayList<ArrayList<Integer>> this_iteration = GenerateCombinationByDigit(digit, last_iteration);

            // Move on to the next iteration (this_iteration becomes last_iteration)
            last_iteration = new ArrayList<>(this_iteration);
        }



        return Arrays.stream(Tools.LLtoAA(last_iteration, Integer.class))
                .map(x -> Arrays.stream(x).mapToInt(Integer::intValue).toArray()).toArray(int[][]::new);
    }

    public static int[][] DivideAndStack(int[][] keywordPossibilities) {
        int halfLength = Math.floorDiv(keywordPossibilities[0].length, 2);

        ArrayList<int[]> keywords = new ArrayList<>();

        for (int[] keyword : keywordPossibilities) {
            int[] alteredKeyword = new int[halfLength];
            for (int i = 0; i < halfLength; i++) {
                alteredKeyword[i] = keyword[i] * 10 + keyword[halfLength + i];
            }
            keywords.add(alteredKeyword);
        }

        return keywords.toArray(int[][]::new);
    }

    private static ArrayList<ArrayList<Integer>> GenerateCombinationByDigit(int[] digit, ArrayList<ArrayList<Integer>> last_iteration) {
        ArrayList<ArrayList<Integer>> this_iteration = new ArrayList<>();

        // Iterate over each possibility in the current row
        for (int possibility : digit) {
            // For each possibility, create altered combinations from the last iteration
            for (ArrayList<Integer> combination : last_iteration) {
                // Create a new combination by copying the current combination
                ArrayList<Integer> newCombination = new ArrayList<>(combination);

                // Add the current possibility to the new combination
                newCombination.add(possibility);

                // Add the new combination to this iteration
                this_iteration.add(newCombination);
            }
        }
        return this_iteration;
    }

    public static int[][] GetKeywords(String ciphertext) {
        int[][] digitPossibilities = GetDigitPossibilities(ciphertext);

        System.out.println(Arrays.deepToString(digitPossibilities));

        int[][] keywordPossibilities = GetKeywordPossibilities(digitPossibilities);

        return DivideAndStack(keywordPossibilities);
    }

    public static String DecryptWithKeyword(String ciphertext, int[] keyword) {
        int keywordLength = keyword.length;
        int[] ciphertextArray = Arrays.stream(ciphertext.split(" ")).mapToInt(Integer::parseInt).toArray();
        int[][] cosets = Tools.MakeIntArrayCosets(ciphertextArray, keywordLength);

        for (int i = 0; i < keywordLength; i++) {
            int keyLetter = keyword[i];
            cosets[i] = Arrays.stream(cosets[i]).map(x -> x - keyLetter).toArray();
        }

        int[] decryptedInts = Tools.InterleaveIntArray(cosets);
        ArrayList<String> chars = new ArrayList<>();

        for (int i : decryptedInts) {
            chars.add(Tools.PolybiusDefault[(i - i % 10) / 10 - 1][i % 10 - 1]);
        }

        return String.join("", chars);
    }


    public static void ConvertWithResultsDialogue(String ciphertext) {
        int[][] keywords = GetKeywords(ciphertext);

        String maxPlaintext = "";
        double minIOCDifference = 100D;

        int[] usedKeyword = new int[] {};

        for (int[] keyword : keywords) {
            String plaintext = DecryptWithKeyword(ciphertext, keyword);
            double indexOfCoincidence = Tools.IndexOfCoincidence(plaintext);

            if (abs(indexOfCoincidence - Tools.EnglishIOC) < minIOCDifference) {
                minIOCDifference = indexOfCoincidence;
                maxPlaintext = plaintext;
                usedKeyword = keyword;
            }
        }

        Main.boxHandler.OpenNihilistOutput(keywords, usedKeyword, maxPlaintext, ciphertext);
    }

    public static void ConvertFromResultsDialogue(String ciphertext, int[][] predictedKeywords, String[] usedKeywordString) {
        int[] usedKeyword = Arrays.stream(usedKeywordString).mapToInt(Integer::parseInt).toArray();
        String plaintext = DecryptWithKeyword(ciphertext, usedKeyword);

        Main.boxHandler.OpenNihilistOutput(predictedKeywords, usedKeyword, plaintext, ciphertext);
    }


}

//  55 14 23 45 51 55 44