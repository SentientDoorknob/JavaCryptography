package org.application.decoders;

import org.application.Main;
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

    public static double[][] GetThheMatrix(String ciphertext) {
        String[] digraphs = Tools.SplitDigraphs(ciphertext);

        HashMap<String, Integer> digraphFrequencies = Tools.Count(digraphs);

        String th = Collections.max(digraphFrequencies.entrySet(), Map.Entry.comparingByValue()).getKey();
        digraphFrequencies.put(th, 0);

        String he = Collections.max(digraphFrequencies.entrySet(), Map.Entry.comparingByValue()).getKey();

        double[][] thhe = new double[][] {{th.charAt(0) - 'a', he.charAt(0) - 'a'}, {th.charAt(1) - 'a', he.charAt(1) - 'a'}};

        return thhe;
    }

    public static String[] ThheMatrixToString(double[][] thhe) {
        return new String[] {"" + ((char)((int)thhe[0][0] + 'a')) + ((char)((int)thhe[1][0] + 'a')),
                "" + ((char)((int)thhe[0][1] + 'a')) + ((char)((int)thhe[1][1] + 'a'))};
    }

    public static double[][] ThheStringToMatrix(String[] thhe) {
        System.out.println(Arrays.toString(thhe));
        return new double[][] {{thhe[0].charAt(0) - 'a', thhe[1].charAt(0) - 'a'}, {thhe[0].charAt(1) - 'a', thhe[1].charAt(1) - 'a'}};
    }

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



    public static void DecryptFromResultsDialogue(String ciphertext, String[] thheString, double[][] predictedMatrix) {
        double[][] thhe = ThheStringToMatrix(thheString);

        double[][] decryptionMatrix = GetDecryptionMatix(thhe);
        double[][] encryptionMatrix = GetEncryptionMatrix(thhe);

        String plaintext = DecryptWithKeyMatrix(ciphertext, decryptionMatrix);
        Main.boxHandler.OpenHillOutput(predictedMatrix, encryptionMatrix, thheString, plaintext, ciphertext);
    }

    public static void DecryptWithResultsDialogue(String ciphertext) {

        double[][] thhe = GetThheMatrix(ciphertext);

        double[][] decryptionMatrix = GetDecryptionMatix(thhe);
        double[][] encryptionMatrix = GetEncryptionMatrix(thhe);

        String plaintext = DecryptWithKeyMatrix(ciphertext, decryptionMatrix);
        String[] thheString = ThheMatrixToString(thhe);

        Main.boxHandler.OpenHillOutput(encryptionMatrix, encryptionMatrix, thheString, plaintext, ciphertext);
    }
}
