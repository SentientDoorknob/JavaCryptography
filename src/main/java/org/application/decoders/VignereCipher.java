package org.application.decoders;

import org.crypography_tools.Tools;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class VignereCipher {
    private static final double VIGNERE_THRESHOLD = 0.005;

    public static String Decrypt(String ciphertext) {
        return "Not Implemented";
    }


    public static double TryKeywordLength(String ciphertext, int length) {
        String[] cosets = Tools.MakeCosets(ciphertext, length);

        double sum = 0;

        for (String set : cosets) {
            sum += Tools.IndexOfCoincidence(set);
        }

        System.out.printf("%s had an IOC of: %f\n", cosets[0], Tools.IndexOfCoincidence(cosets[0]));

        double averageIOC = sum / length;

        return abs(averageIOC - Tools.EnglishIOC);
    }


    public static int GetKeywordLength(String ciphertext, int maxLength) {

        double[] results = new double[maxLength + 1];

        results[0] = 100;

        for (int i = 1; i <= maxLength; i++) {
            results[i] = TryKeywordLength(ciphertext, i);
        }

        System.out.println(Arrays.toString(results));

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
            System.out.println(Arrays.toString(frequencies));

            int min = Arrays.stream(frequencies).max().orElseThrow();
            int imin = Arrays.stream(frequencies).boxed().collect(Collectors.toList()).indexOf(min);

            char keyword_character = (char) (Math.floorMod((imin - 4), 26) + 'a');

            System.out.printf("CHAR %s\n", keyword_character);

            keyword += keyword_character;
            keyword += " ";
        }

        return keyword;
    }


    public static String GetKeyword(String ciphertext) {
        int KeywordLength = GetKeywordLength(ciphertext, 20);

        return GetKeywordWithGraphAndLength(ciphertext, KeywordLength);
    }
}
