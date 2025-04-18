package org.application.decoders;

import org.application.DialogueBoxHandler;
import org.application.Main;
import org.application.results.cipher.NihilistResult;
import org.application.results.cipher.VignereResult;
import org.crypography_tools.Tools;
import org.crypography_tools.UnitTable;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.abs;
import static java.lang.Math.max;

//public class NihilistCipher {
//
//    // Okay so im like not doing this
//
//    public static boolean[] EvaluateUnits(int[] coset) {
//        boolean[] hasEnding = new boolean[10];
//
//        for (int i : coset) {
//            int units = i % 10;
//            int index = Math.floorMod(units - 1, 10);
//
//            hasEnding[index] = true;
//        }
//
//        return hasEnding;
//    }
//
//    public static boolean[] EvaluateTens(int[] coset) {
//        boolean[] hasEnding = new boolean[10];
//
//        for (int i : coset) {
//            int units = (i % 100 - i % 10) / 10;
//            int index = Math.floorMod(units - 1, 10);
//
//            hasEnding[index] = true;
//        }
//
//        return hasEnding;
//    }
//
//    public static int[] GetSpans(boolean[][] bools) {
//        int length = bools.length;
//        int[] spans = new int[length];
//
//        for (int i = 0; i < length; i++) {
//            int[] trues = Tools.GetTrues(bools[i]).stream().mapToInt(Integer::intValue).toArray();
//            int maxIndex = Arrays.stream(trues).max().orElse(72);
//            int minIndex = Arrays.stream(trues).min().orElse(0);
//
//            spans[i] = maxIndex - minIndex + 1;
//        }
//
//        return spans;
//    }
//
//    public static int[][] GetDigits(boolean[][] bools) {
//        int keywordLength = bools.length;
//        int[][] lastDigits = new int[keywordLength][5];
//
//        for (int[] digitOption : lastDigits) {
//            Arrays.fill(digitOption, -1);
//        }
//
//        for (int i = 0; i < keywordLength; i++) {
//            int[] span = Tools.GetMaximumSpan(bools[i]);
//            // span, starting value
//
//            if (span[0] >= 5) {
//                lastDigits[i][0] = span[1];
//            }
//            else {
//                int numPossibilities = 5 - span[0];
//
//                for (int j = 0; j < numPossibilities; j++) {
//                    lastDigits[i][j] = Math.floorMod(span[0] - j, 10);
//                }
//            }
//        }
//
//        return lastDigits;
//    }
//
//    public static boolean[][] GetCosetTableIter(String ciphertext, int keywordLength, boolean evaluate10s) {
//
//        int[] ints = Arrays.stream(ciphertext.split(" ")).mapToInt(Integer::parseInt).toArray();
//        int[][] cosets = Tools.MakeIntArrayCosets(ints, keywordLength);
//
//        boolean[][] results = new boolean[keywordLength][10];
//
//        for (int i = 0; i < keywordLength; i++) {
//            results[i] = evaluate10s? EvaluateTens(cosets[i]) : EvaluateUnits(cosets[i]);
//        }
//
//        int[] spans = GetSpans(results);
//        boolean isValid = Arrays.stream(spans).allMatch(x -> x <= 5);
//
//        if (!isValid) {
//            return GetCosetTableIter(ciphertext, keywordLength + 1, evaluate10s);
//        }
//
//        return results;
//    }
//
//    public static boolean[][] GetCosetTableFixed(String ciphertext, int keywordLength, boolean evaluate10s) {
//        int[] ints = Arrays.stream(ciphertext.split(" ")).mapToInt(Integer::parseInt).toArray();
//        int[][] cosets = Tools.MakeIntArrayCosets(ints, keywordLength);
//
//        boolean[][] results = new boolean[keywordLength][10];
//
//        for (int i = 0; i < keywordLength; i++) {
//            results[i] = evaluate10s? EvaluateTens(cosets[i]) : EvaluateUnits(cosets[i]);
//        }
//
//        int[] spans = GetSpans(results);
//        boolean isValid = Arrays.stream(spans).allMatch(x -> x <= 5);
//
//        return results;
//    }
//
//    public static int[][] TrimResults(int[][] input, int value) {
//        ArrayList<int[]> filteredList = new ArrayList<>();
//
//        for (int[] row : input) {
//            int[] newRow = Arrays.stream(row).filter(x -> x != value).toArray();
//            filteredList.add(newRow);
//        }
//
//        return filteredList.toArray(new int[0][]);
//    }
//
//    public static int[][] GetDigitPossibilities(String ciphertext) {
//        boolean[][] results = GetCosetTableIter(ciphertext, 1, false);
//        int[][] lastDigits = TrimResults(GetDigits(results), -1);
//
//        int keywordLength = results.length;
//
//        boolean[][] resultsTens = GetCosetTableFixed(ciphertext, keywordLength, true);
//        int[][] firstDigits = TrimResults(GetDigits(resultsTens), -1);
//
//        System.out.println(Arrays.deepToString(lastDigits));
//        System.out.println(Arrays.deepToString(firstDigits));
//        System.out.println();
//
//        return Stream.concat(Arrays.stream(firstDigits), Arrays.stream(lastDigits))
//                .toArray(int[][]::new);
//    }
//
//    public static int[][] GetKeywordPossibilities(int[][] digitPossibilities) {
//        ArrayList<ArrayList<Integer>> last_iteration = new ArrayList<>();
//        last_iteration.add(new ArrayList<>());
//
//        for (int[] digit : digitPossibilities) {
//            ArrayList<ArrayList<Integer>> this_iteration = GenerateCombinationByDigit(digit, last_iteration);
//
//            // Move on to the next iteration (this_iteration becomes last_iteration)
//            last_iteration = new ArrayList<>(this_iteration);
//        }
//
//
//
//        return Arrays.stream(Tools.LLtoAA(last_iteration, Integer.class))
//                .map(x -> Arrays.stream(x).mapToInt(Integer::intValue).toArray()).toArray(int[][]::new);
//    }
//
//    public static int[][] DivideAndStack(int[][] keywordPossibilities) {
//        int halfLength = Math.floorDiv(keywordPossibilities[0].length, 2);
//
//        ArrayList<int[]> keywords = new ArrayList<>();
//
//        for (int[] keyword : keywordPossibilities) {
//            int[] alteredKeyword = new int[halfLength];
//            for (int i = 0; i < halfLength; i++) {
//                alteredKeyword[i] = keyword[i] * 10 + keyword[halfLength + i];
//            }
//            keywords.add(alteredKeyword);
//        }
//
//        return keywords.toArray(int[][]::new);
//    }
//
//    private static ArrayList<ArrayList<Integer>> GenerateCombinationByDigit(int[] digit, ArrayList<ArrayList<Integer>> last_iteration) {
//        ArrayList<ArrayList<Integer>> this_iteration = new ArrayList<>();
//
//        // Iterate over each possibility in the current row
//        for (int possibility : digit) {
//            // For each possibility, create altered combinations from the last iteration
//            for (ArrayList<Integer> combination : last_iteration) {
//                // Create a new combination by copying the current combination
//                ArrayList<Integer> newCombination = new ArrayList<>(combination);
//
//                // Add the current possibility to the new combination
//                newCombination.add(possibility);
//
//                // Add the new combination to this iteration
//                this_iteration.add(newCombination);
//            }
//        }
//        return this_iteration;
//    }
//
//    public static int[][] GetKeywords(String ciphertext) {
//        int[][] digitPossibilities = GetDigitPossibilities(ciphertext);
//
//        System.out.println(Arrays.deepToString(digitPossibilities));
//
//        int[][] keywordPossibilities = GetKeywordPossibilities(digitPossibilities);
//
//        return DivideAndStack(keywordPossibilities);
//    }
//
//    public static String DecryptWithKeyword(String ciphertext, int[] keyword) {
//        int keywordLength = keyword.length;
//        int[] ciphertextArray = Arrays.stream(ciphertext.split(" ")).mapToInt(Integer::parseInt).toArray();
//        int[][] cosets = Tools.MakeIntArrayCosets(ciphertextArray, keywordLength);
//
//        for (int i = 0; i < keywordLength; i++) {
//            int keyLetter = keyword[i];
//            cosets[i] = Arrays.stream(cosets[i]).map(x -> x - keyLetter).toArray();
//        }
//
//        int[] decryptedInts = Tools.InterleaveIntArray(cosets);
//        ArrayList<String> chars = new ArrayList<>();
//
//        for (int i : decryptedInts) {
//            chars.add(Tools.PolybiusDefault[(i - i % 10) / 10 - 1][i % 10 - 1]);
//        }
//
//        return String.join("", chars);
//    }
//
//
//    public static void ConvertWithResultsDialogue(String ciphertext) {
//        int[][] keywords = GetKeywords(ciphertext);
//
//        String maxPlaintext = "";
//        double minIOCDifference = 100D;
//
//        int[] usedKeyword = new int[] {};
//
//        for (int[] keyword : keywords) {
//            String plaintext = DecryptWithKeyword(ciphertext, keyword);
//            double indexOfCoincidence = Tools.IndexOfCoincidence(plaintext);
//
//            if (abs(indexOfCoincidence - Tools.EnglishIOC) < minIOCDifference) {
//                minIOCDifference = indexOfCoincidence;
//                maxPlaintext = plaintext;
//                usedKeyword = keyword;
//            }
//        }
//
//        Main.boxHandler.OpenNihilistOutput(keywords, usedKeyword, maxPlaintext, ciphertext);
//    }
//
//    public static void ConvertFromResultsDialogue(String ciphertext, int[][] predictedKeywords, String[] usedKeywordString) {
//        int[] usedKeyword = Arrays.stream(usedKeywordString).mapToInt(Integer::parseInt).toArray();
//        String plaintext = DecryptWithKeyword(ciphertext, usedKeyword);
//
//        Main.boxHandler.OpenNihilistOutput(predictedKeywords, usedKeyword, plaintext, ciphertext);
//    }
//
//
//}

/*

    Method Source: https://toebes.com/codebusters/Samples/CodeBusters_Overview.pdf pg. 83, 20.A

    boolean[][]  TRY_UT -> Unit Table Component for specific attempted key length
    UnitTable    UT -> Sorts automatically according to SORT_A

    boolean[][]  UN_COMP -> The Unit Table Component that goes with the KEYLEN
    int[][]        UNITS -> Possible Key Segments, for the units in the key numbers

    boolean[][]  TE_COMP -> Tens Table Component.
    int[][]        TENS -> Possible Key Segments, for the tens in the key numbers

    int          KEYLEN -> Length of Keyword
    int[][]      KEYS -> Possible keys from data.

    String[]     SUBTEXTS -> Possible subtexts from possible keys

    NihilistResult[] RESULTS -> Possible Results. Best selected based on Index Of Coincidence.

    Note: KEYLEN and COMP are packaged as 1 Map.Entry(), ENTRY. Note ENTRY.GetKey = COMP, ENTRY.GetValue = KEYLEN.

    SORT_A:
    1. According to
        # cases that look like the following: false true false | false true true | true true false (ascending)
        + # false
    2. According to associated keyword length (ascending)

 */

public class NihilistCipher {
    // CIPHERTEXT -> TRY_UT -> UT -> KEYLEN  -> TE_COMP -> TENS -> KEYS -> SUBTEXTS -> RESULTS -> RESULT
    //                            -> UN_COMP ->      UNITS      ->

    // UTIL
    private static void PrintBoolTable(boolean[][] table) {
        if (table == null) {
            System.out.println("Null Table \n");
            return;
        }

        System.out.println("[");
        for (boolean[] row : table) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println("]\n");
    }

    // UTIL  KEY -> SUBTEXT
    public static String DecryptWithKey(int[] ciphertext, int[] key) {
        int[][] cosets = Tools.MakeIntArrayCosets(ciphertext, key.length);

        for (int i = 0; i < cosets.length; i++) {
            for (int j = 0; j < cosets[i].length; j++) {
                cosets[i][j] -= key[i];
            }
        }

        int[] plaintextInts = Tools.InterleaveIntArray(cosets);
        String output = "";

        for (int chr : plaintextInts) {
            int x = chr % 10;
            int y = (chr - x) / 10;

            output += Tools.PolybiusDefault[x - 1][y - 1];
        }

        return output;
    }

    // Returns TRY_UT, null. Null if table is invalid (see Method Source).
    public static boolean[][] TryKeywordLength(int[] ciphertext, int tryLength) {
        boolean[][] table = new boolean[tryLength][10];
        int[][] cosets = Tools.MakeIntArrayCosets(ciphertext, tryLength);

        for (int i = 0; i < tryLength; i++) {
            for (int x : cosets[i]) {
                int unit = x % 10;
                table[i][unit] = true;
            }

            int count = 0;
            for (boolean b : table[i]) {
                if (b) count++;
            }

            if (count > 5) return null;
        }

        return table;
    }

    // Returns UT. Doesn't contain any null tables, only valid ones. Best are first in the map. Gets TRY_UT.
    public static UnitTable GetUnitTable(int[] ciphertext, int MaxKeywordLength) {
        UnitTable table = new UnitTable();

        for (int i = 2; i < MaxKeywordLength; i++) {
            boolean[][] component = TryKeywordLength(ciphertext, i);
            if (component == null) continue;

            table.Put(component);
        }

        return table;
    }

    // Returns KEYLEN & UN_COMP in ENTRY. Gets UT.
    public static Map.Entry<boolean[][], Integer> GetKeyLength(int[] ciphertext, int MaxKeywordLength) {
        return GetUnitTable(ciphertext, MaxKeywordLength).GetFirstEntry();
    }

    // Returns TE_COMP. Takes in KEYLEN.
    public static boolean[][] GetTensComponent(int[] ciphertext, int length) {
        boolean[][] table = new boolean[length][10];
        int[][] cosets = Tools.MakeIntArrayCosets(ciphertext, length);

        for (int i = 0; i < length; i++) {
            for (int x : cosets[i]) {
                int ten = ((x - (x % 10)) / 10) % 10;
                table[i][ten] = true;
            }
        }

        return table;
    }

    // Returns A. UNITS, B. TENS. Given A. UN_COMP, B. TE_COMP
    public static int[][] GetDigitPossibilitiesFromTableComponent(boolean[][] comp) {
        int[][] possibilities = new int[comp.length][];

        for (int i = 0; i < comp.length; i++) {
            int[] span = Tools.GetSpan(comp[i]);
            int[] options = Tools.GetSpanStartingPossibilities(span);
            possibilities[i] = options;
        }

        return possibilities;
    }

    // Returns KEYS
    public static int[][] GetPossibleKeys(int[][] units, int[][] tens) {
        int length = units.length;
        int[][] digitPossibilities = new int[length][];

        for (int i = 0; i < length; i++) {
            ArrayList<Integer> possibilities = new ArrayList<>();
            for (int unitPossibility : units[i]) {
                for (int tensPossibility : tens[i]) {
                    possibilities.add(10 * tensPossibility + unitPossibility);
                }
            }

            digitPossibilities[i] = possibilities.stream().mapToInt(Integer::intValue).toArray();
        }

        return Tools.CartesianProduct(digitPossibilities, 0).toArray(int[][]::new);
    }

    // Returns RESULTS. Takes in keys and evaluates them.
    public static NihilistResult[] GetPossibleResults(int[] ciphertext, int[][] keys) {
        ArrayList<NihilistResult> results = new ArrayList<>();

        for (int[] key : keys) {
            String ciphertextString = Arrays.stream(ciphertext).mapToObj(String::valueOf).collect(Collectors.joining(" "));
            String subtext = DecryptWithKey(ciphertext, key);
            double IoC = Tools.IndexOfCoincidence(subtext);
            NihilistResult result = new NihilistResult(ciphertextString, subtext, keys, key, IoC, NihilistCipher::ConvertFromResultsDialogue);
            results.add(result);
        }

        return results.toArray(new NihilistResult[0]);
    }

    // Returns RESULT. Chooses the NihilistResult with the closest IoC to English. Takes in RESULTS.
    public static NihilistResult GetBestResult(NihilistResult[] results) {
        double minDifference = 10;
        NihilistResult bestResult = null;

        for (NihilistResult result : results) {
            double difference = abs(Tools.EnglishIOC - result.IoC);
            if (difference < minDifference) {
                minDifference = difference;
                bestResult = result;
            }
        }

        return bestResult;
    }



    public static void ConvertWithResultsDialogue(String in) {
        int[] ciphertext = Arrays.stream(in.split(" ")).mapToInt(Integer::parseInt).toArray();

        int keywordLength = GetKeyLength(ciphertext, 20).getValue();

        System.out.println(keywordLength);

        boolean[][] unitsComponent = GetKeyLength(ciphertext, 20).getKey(); // sorry, confusing i know. java = wierd.
        boolean[][] tensComponent = GetTensComponent(ciphertext, keywordLength);

        int[][] unitsPossibilities = GetDigitPossibilitiesFromTableComponent(unitsComponent);
        int[][] tensPossibilities = GetDigitPossibilitiesFromTableComponent(tensComponent);

        System.out.println(Arrays.deepToString(unitsPossibilities));
        System.out.println(Arrays.deepToString(tensPossibilities));

        int[][] keyPossibilities = GetPossibleKeys(unitsPossibilities, tensPossibilities);
        String plaintext = DecryptWithKey(ciphertext, keyPossibilities[0]);

        NihilistResult[] results = GetPossibleResults(ciphertext, keyPossibilities);
        NihilistResult result = GetBestResult(results);

        Main.boxHandler.OpenNihilistOutput(result);
    }


    public static void ConvertFromResultsDialogue(NihilistResult result) {
        System.out.println(Arrays.toString(result.usedKeyword));
        int[] ciphertext = Tools.StringToIntArray(result.ciphertext.split(" "));
        result.plaintext = DecryptWithKey(ciphertext, result.usedKeyword);
        result.IoC = Tools.IndexOfCoincidence(result.plaintext);
        Main.boxHandler.OpenNihilistOutput(result);
    }


}

//  55 14 23 45 51 55 44