package org.application.decoders;

import org.crypography_tools.Tools;

public class VignereCipher {
    public static String Decrypt(String ciphertext) {
        return "Not Implemented";
    }

    public static int TryKeywordLength(String ciphertext, int length) {
        String[] cosets = Tools.MakeCosets(ciphertext, length);

        int sum = 0;

        for (String set : cosets) {
            sum +=
        }
    }

    public static int GetKeywordLength(String ciphertext, int maxLength) {



        return 0;
    }

    public static String GetKeyword(String ciphertext) {
        return "Not Implemented";
    }
}
