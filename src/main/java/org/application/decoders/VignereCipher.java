package org.application.decoders;

import org.crypography_tools.Tools;

import javax.tools.Tool;

public class VignereCipher {
    public static String Decrypt(String ciphertext) {
        return "Not Implemented";
    }

    public static double TryKeywordLength(String ciphertext, int length) {
        String[] cosets = Tools.MakeCosets(ciphertext, length);

        int sum = 0;

        for (String set : cosets) {
            sum += Tools.IndexOfCoincidence(set);
        }

        double averageIOC = sum / length;

        return averageIOC - Tools.EnglishIOC;
    }

    public static int GetKeywordLength(String ciphertext, int maxLength) {



        return 0;
    }

    public static String GetKeyword(String ciphertext) {
        int KeywordLength = GetKeywordLength(ciphertext, 20);

        return "N/I";
    }
}
