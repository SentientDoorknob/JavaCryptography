package org.application.decoders;

import org.crypography_tools.LinearAlgebra;
import org.crypography_tools.Tools;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HillCipher {
    public static double[][] GetEncryptionMatrix(double[][] thhe) {
        double[][] con = new double[][] {{19, 7}, {7, 4}};

        double[][] inv_matrix = LinearAlgebra.MxMod(thhe, LinearAlgebra._2x2InverseMod(con, 26), 26);

        return inv_matrix;
    }

    public static double[][] GetDecryptionMatix(double[][] thhe) {
        double[][] con = new double[][] {{19, 7}, {7, 4}};

        double[][] matrix = LinearAlgebra.MxMod(con, LinearAlgebra._2x2InverseMod(thhe, 26), 26);

        return matrix;
    }

    public static void DecryptWithResultsDialogue(String ciphertext) {
        String[] digraphs = Tools.SplitDigraphs(ciphertext);
        HashMap<String, Integer> digraphFrequencies = Tools.Count(digraphs);

        String th = Collections.max(digraphFrequencies.entrySet(), Map.Entry.comparingByValue()).getKey();
        digraphFrequencies.put(th, 0);

        String he = Collections.max(digraphFrequencies.entrySet(), Map.Entry.comparingByValue()).getKey();

        double[][] thhe = new double[][] {{th.charAt(0) - 'a', he.charAt(0) - 'a'}, {th.charAt(1) - 'a', he.charAt(1) - 'a'}};

        double[][] decryptionMatrix = GetDecryptionMatix(thhe);

        LinearAlgebra.DisplayMatrix(thhe);
        LinearAlgebra.DisplayMatrix(decryptionMatrix);

        String plaintext = "";

        for (String digraph : digraphs) {
            double[][] decrypted = LinearAlgebra.MxMod(decryptionMatrix, new double[][] {{digraph.charAt(0) - 'a'}, {digraph.charAt(1) - 'a'}}, 26);
            plaintext += (char)((int) decrypted[0][0] + 'a');
            plaintext += (char)((int) decrypted[1][0] + 'a');
        }

        System.out.println(plaintext);
    }
}
