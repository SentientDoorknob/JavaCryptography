package org.application;

import org.application.decoders.*;
import org.crypography_tools.Tools;

import java.util.Arrays;

public class Main {

    public static DialogueBoxHandler boxHandler;

    public static String cipher;

    public static void main(String[] args) {
        boxHandler = new DialogueBoxHandler();

        String[] ciphers = new String[] {"Vignere Cipher", "Affine Cipher", "Permutation Cipher", "Bifid Cipher", "Hill Cipher", "Nihilist Cipher"};

        boxHandler.OpenCipherInputDialogue(ciphers);

        int[][] c = new int[][] {{1}, {2}, {3,4}, {5,6}};

        System.out.println(Arrays.deepToString(NihilistCipher.GetKeywordPossibilities(c)));

    }

    public static void OnCipherSelection(String cipherInput) {
        cipher = cipherInput;
        boxHandler.OpenCiphertextInputDialogue();
    }

    public static void OnCipherTextInput(String ciphertext) {

        String f_text = Tools.Format(ciphertext);
        String b_text = Tools.DigitFormat(ciphertext);
        String n_text = Tools.NumberFormat(ciphertext);

        switch (cipher) {
            case "Vignere Cipher" -> VignereCipher.DecryptWithResultsDialogue(f_text);
            case "Affine Cipher" -> AffineCipher.DecryptWithResultsDialogue(f_text);
            case "Permutation Cipher" -> PermutationCipher.DecryptWithResultsDialogue(f_text);
            case "Hill Cipher" -> HillCipher.DecryptWithResultsDialogue(f_text);
            case "Bifid Cipher" -> BifidCipher.ConvertWithResultsDialogue(b_text);
            case "Nihilist Cipher" -> NihilistCipher.ConvertWithResultsDialogue(n_text);
        }

    }
}