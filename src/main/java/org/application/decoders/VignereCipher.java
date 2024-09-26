package org.application.decoders;

import org.application.Main;
import org.crypography_tools.Tools;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class VignereCipher {
    public static final double VIGNERE_THRESHOLD = 0.005;
    public static final int MAX_KEYWORD_LENGTH = 30;

    public static void DecryptWithResultsDialogue(String ciphertext) {
        ciphertext = Tools.Format(ciphertext);

        int keywordLength = GetKeywordLength(ciphertext, MAX_KEYWORD_LENGTH);
        String keyword = GetKeywordWithGraphAndLength(ciphertext, keywordLength);
        String plaintext = DecryptWithKeyword(ciphertext, keyword);

        Main.boxHandler.OpenVignereOutput(keyword, plaintext, ciphertext);
    }

    public static void DecryptFromDialogue(String ciphertext, String keyword) {
        ciphertext = Tools.Format(ciphertext);
        String plaintext = DecryptWithKeyword(ciphertext, keyword);

        Main.boxHandler.OpenVignereOutput(keyword, plaintext, ciphertext);
    }



    public static double TryKeywordLength(String ciphertext, int length) {
        String[] cosets = Tools.MakeCosets(ciphertext, length);

        double sum = 0;

        for (String set : cosets) {
            sum += Tools.IndexOfCoincidence(set);
        }


        double averageIOC = sum / length;

        return abs(averageIOC - Tools.EnglishIOC);
    }


    public static int GetKeywordLength(String ciphertext, int maxLength) {

        double[] results = new double[maxLength + 1];

        results[0] = 100;

        for (int i = 1; i <= maxLength; i++) {
            results[i] = TryKeywordLength(ciphertext, i);
        }


        double min = Arrays.stream(results).filter(x -> x < VIGNERE_THRESHOLD).toArray()[0];
        int imin = Arrays.stream(results).boxed().collect(Collectors.toList()).indexOf(min);

        return imin;
    }


    public static String GetKeywordWithLength(String ciphertext, int length) {
        return "N/I";
    }


    public static String GetKeywordWithGraphAndLength(String ciphertext, int length) {
        String[] cosets = Tools.MakeCosets(ciphertext, length);

        String keyword = "";

        for (String set : cosets) {
            int[] frequencies = Tools.AbsoluteFrequency(set);

            int min = Arrays.stream(frequencies).max().orElseThrow();
            int imin = Arrays.stream(frequencies).boxed().collect(Collectors.toList()).indexOf(min);

            char keyword_character = (char) (Math.floorMod((imin - 4), 26) + 'a');


            keyword += keyword_character;
        }

        return keyword;
    }


    public static String GetKeyword(String ciphertext) {
        int KeywordLength = GetKeywordLength(ciphertext, 20);

        return GetKeywordWithGraphAndLength(ciphertext, KeywordLength);
    }

    public static String DecryptWithKeyword(String ciphertext, String keyword) {
        int keywordLength = keyword.length();
        String[] cosets = Tools.MakeCosets(ciphertext, keywordLength);

        for (int i = 0; i < keywordLength; i++) {

            cosets[i] = Tools.ShiftLetters(cosets[i], (keyword.charAt(i) - 'a') * -1);
        }

        return Tools.Interleave(cosets);
    }
}
