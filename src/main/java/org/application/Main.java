package org.application;

import org.application.decoders.AffineCipher;
import org.application.decoders.VignereCipher;
import org.crypography_tools.Tools;

import java.util.Arrays;

public class Main {

    public static DialogueBoxHandler boxHandler;

    public static String cipher;

    public static void main(String[] args) {
        boxHandler = new DialogueBoxHandler();

        String[] ciphers = new String[] {"Vignere Cipher", "Affine Cipher"};

        boxHandler.OpenCipherInputDialogue(ciphers);

    }

    public static void OnCipherSelection(String cipherInput) {
        cipher = cipherInput;
        boxHandler.OpenCiphertextInputDialogue();
    }

    public static void OnCipherTextInput(String ciphertext) {

        String f_text = Tools.Format(ciphertext);

        switch (cipher) {
            case "Vignere Cipher" -> VignereCipher.DecryptWithResultsDialogue(f_text);
            case "Affine Cipher" -> AffineCipher.DecryptWithResultsDialogue(f_text);
        }

    }
}