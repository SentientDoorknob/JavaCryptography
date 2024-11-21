package org.application.decoders;

import org.application.Main;
import org.application.results.cipher.VignereResult;
import org.crypography_tools.Tools;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

/*

    Method Source: https://pages.mtu.edu/~shene/NSF-4/Tutorial/VIG/Vig-Base.html and subsequent articles

    IOCD -> Calculated IoC - English IoC.
    KEYLEN -> Length of keyword.
    KEY -> String of letters, not AIs.

 */

public class VignereCipher {
    public static final double VIGNERE_THRESHOLD = 0.005;
    public static final int MAX_KEYWORD_LENGTH = 30;

    // CIPHERTEXT -> KEYLEN -> KEY -> PLAINTEXT

    // Returns IOCD. Generates cosets given length and calculates average IoC.
    private static double TryKeywordLength(String ciphertext, int length) {
        String[] cosets = Tools.MakeCosets(ciphertext, length);

        double sum = 0;

        for (String set : cosets) {
            sum += Tools.IndexOfCoincidence(set);
        }

        double averageIOC = sum / length;

        System.out.println(averageIOC);

        return abs(averageIOC - Tools.EnglishIOC);
    }

    // Returns KEYLEN. Tries lengths up to max, and gets smallest less than vignere threshold.
    public static int GetKeywordLength(String ciphertext, int maxLength) {

        double[] results = new double[maxLength + 1];

        results[0] = 100;

        for (int i = 1; i <= maxLength; i++) {
            results[i] = TryKeywordLength(ciphertext, i);
        }

        System.out.println(Arrays.toString(results));

        double min = Arrays.stream(results).filter(x -> x < VIGNERE_THRESHOLD).toArray()[0]; // Filter to less than Vignere Threshold
        int imin = Arrays.stream(results).boxed().toList().indexOf(min); // Get index of smallest

        return imin;
    }

    // Returns KEY. For each coset, runs X2 analysis - see method.
    public static String GetKeywordWithLength(String ciphertext, int length) {
        String[] cosets = Tools.MakeCosets(ciphertext, length);
        String keyword = "";

        for (String coset : cosets) {

            double minX2 = 100;
            int minIndex = 0;

            for (int i = 0; i < 26; i++) {
                coset = Tools.ShiftLetters(coset, -1);
                double cosetX2 = Tools.XSquaredEnglish(coset);

                if (cosetX2 < minX2) {
                    minX2 = cosetX2;
                    minIndex = i;
                }
            }

            int index = (minIndex + 1) % 26;

            keyword += (char) (index + 'a');
        }

        return keyword;
    }

    // Returns PLAINTEXT. Shifts cosets by respective keyword char.
    public static String DecryptWithKeyword(String ciphertext, String keyword) {
        int keywordLength = keyword.length();
        String[] cosets = Tools.MakeCosets(ciphertext, keywordLength);

        for (int i = 0; i < keywordLength; i++) {

            cosets[i] = Tools.ShiftLetters(cosets[i], (keyword.charAt(i) - 'a') * -1);
        }

        return Tools.Interleave(cosets);
    }


    public static void DecryptWithResultsDialogue(String ciphertext) {
        ciphertext = Tools.Format(ciphertext);

        int keywordLength = GetKeywordLength(ciphertext, MAX_KEYWORD_LENGTH);
        String keyword = GetKeywordWithLength(ciphertext, keywordLength);
        String plaintext = DecryptWithKeyword(ciphertext, keyword);

        VignereResult result = new VignereResult(ciphertext, plaintext, keyword, VignereCipher::DecryptFromDialogue);

        Main.boxHandler.OpenVignereOutput(result);
    }

    public static void DecryptFromDialogue(VignereResult result) {
        result.plaintext = DecryptWithKeyword(result.ciphertext, result.usedKeyword);

        Main.boxHandler.OpenVignereOutput(result);
    }
}
