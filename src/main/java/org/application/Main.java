package org.application;

import org.application.decoders.AffineCipher;
import org.application.decoders.BifidCipher;
import org.application.decoders.PermutationCipher;
import org.application.decoders.VignereCipher;
import org.crypography_tools.Tools;

import java.util.Arrays;

public class Main {

    public static DialogueBoxHandler boxHandler;

    public static String cipher;

    public static void main(String[] args) {
        boxHandler = new DialogueBoxHandler();

        String[] ciphers = new String[] {"Vignere Cipher", "Affine Cipher", "Permutation Cipher", "Bifid Cipher"};

        boxHandler.OpenCipherInputDialogue(ciphers);

    }

    public static void OnCipherSelection(String cipherInput) {
        cipher = cipherInput;
        boxHandler.OpenCiphertextInputDialogue();
    }

    public static void OnCipherTextInput(String ciphertext) {

        String f_text = Tools.Format(ciphertext);
        String b_text = Tools.NumberFormat(ciphertext);

        System.out.println(BifidCipher.ToSubustitutionCipher2(b_text));

        switch (cipher) {
            case "Vignere Cipher" -> VignereCipher.DecryptWithResultsDialogue(f_text);
            case "Affine Cipher" -> AffineCipher.DecryptWithResultsDialogue(f_text);
            case "Permutation Cipher" -> PermutationCipher.DecryptWithResultsDialogue(f_text);
            case "Bifid Cipher" -> System.out.println("N/I");
        }

    }
}