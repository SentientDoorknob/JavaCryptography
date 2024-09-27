package org.application;

import org.application.decoders.AffineCipher;
import org.application.decoders.VignereCipher;
import org.crypography_tools.Tools;

import java.util.Arrays;

public class Main {

    public static DialogueBoxHandler boxHandler;

    public static void main(String[] args) {
        boxHandler = new DialogueBoxHandler();

        boxHandler.OpenCipherDialogue();

    }

    public static void OnCipherTextInput(String ciphertext) {

        String f_text = Tools.Format(ciphertext);

        AffineCipher.DecryptWithResultsDialogue(f_text);

    }
}