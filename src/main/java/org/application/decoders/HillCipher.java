package org.application.decoders;

import org.application.Main;
import org.application.results.cipher.HillResult;
import org.crypography_tools.LinearAlgebra;
import org.crypography_tools.Tools;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*
    Note to self: this website is soooo good
    Method Source: https://sites.wcsu.edu/mbxml/html/hill_decrypt_section.html#hill_ciphertext_only

    All matrices are double[][] - see LinearAlgebra.java

    ENCM -> Encryption Matrix.
    DCRM -> Decryption Matrix.
    THHE -> double{{T, H}, {H, E}}

 */

public class HillCipher {

    // CIPHERTEXT -> THHE -> DCRM -> PLAINTEXT
    //                    -> ENCM

    // Returns THHE. Assumes TH is the most common digraph, and HE the second.
    public static double[][] GetThheMatrix(String ciphertext) {
        String[] digraphs = Tools.SplitDigraphs(ciphertext);

        HashMap<String, Integer> digraphFrequencies = Tools.Count(digraphs);

        String th = Collections.max(digraphFrequencies.entrySet(), Map.Entry.comparingByValue()).getKey();
        digraphFrequencies.put(th, 0);

        String he = Collections.max(digraphFrequencies.entrySet(), Map.Entry.comparingByValue()).getKey();

        return new double[][] {{th.charAt(0) - 'a', he.charAt(0) - 'a'}, {th.charAt(1) - 'a', he.charAt(1) - 'a'}};
    }

    // Returns ENCM. Solves for ENCM by subtracting equations. See method.
    public static double[][] GetEncryptionMatrix(double[][] thhe) {
        double[][] con = new double[][] {{19, 7}, {7, 4}};

        return LinearAlgebra.MxMod(thhe, LinearAlgebra._2x2InverseMod(con, 26), 26);
    }

    // Returns DCRM. Solves for DCRM (inverse of ENCM). See method.
    public static double[][] GetDecryptionMatrix(double[][] thhe) {
        double[][] con = new double[][] {{19, 7}, {7, 4}};

        return LinearAlgebra.MxMod(con, LinearAlgebra._2x2InverseMod(thhe, 26), 26);
    }

    // Returns PLAINTEXT. Multiplies digraphs by DCRM. See method.
    public static String DecryptWithKeyMatrix(String ciphertext, double[][] matrix) {
        String[] digraphs = Tools.SplitDigraphs(ciphertext);
        String plaintext = "";

        for (String digraph : digraphs) {
            double[][] decrypted = LinearAlgebra.MxMod(matrix, new double[][] {{digraph.charAt(0) - 'a'}, {digraph.charAt(1) - 'a'}}, 26);
            plaintext += (char)((int) decrypted[0][0] + 'a');
            plaintext += (char)((int) decrypted[1][0] + 'a');
        }

        return plaintext;
    }



    public static void DecryptFromResultsDialogue(HillResult result) {

        double[][] decryptionMatrix = GetDecryptionMatrix(result.thhe);
        double[][] encryptionMatrix = GetEncryptionMatrix(result.thhe);

        result.plaintext = DecryptWithKeyMatrix(result.ciphertext, decryptionMatrix);
        Main.boxHandler.OpenHillOutput(result);
    }

    public static void DecryptWithResultsDialogue(String ciphertext) {

        double[][] thhe = GetThheMatrix(ciphertext);

        double[][] decryptionMatrix = GetDecryptionMatrix(thhe);
        double[][] encryptionMatrix = GetEncryptionMatrix(thhe);

        String plaintext = DecryptWithKeyMatrix(ciphertext, decryptionMatrix);

        HillResult result = new HillResult(ciphertext, plaintext, encryptionMatrix, thhe, HillCipher::DecryptFromResultsDialogue);

        Main.boxHandler.OpenHillOutput(result);
    }
}
