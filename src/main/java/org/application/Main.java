package org.application;

import org.application.decoders.VignereCipher;
import org.crypography_tools.Tools;

public class Main {

    public static DialogueBoxHandler boxHandler;

    public static void main(String[] args) {
        boxHandler = new DialogueBoxHandler();

        boxHandler.OpenCipherDialogue();

    }

    public static void OnCipherTextInput(String ciphertext) {

        String f_text = Tools.Format(ciphertext);

        String[] cosets = Tools.MakeCosets(f_text, 3);

        for (String coset : cosets) {
        }

        VignereCipher.DecryptWithResultsDialogue(f_text);

    }
}