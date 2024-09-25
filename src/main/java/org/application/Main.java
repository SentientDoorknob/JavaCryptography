package org.application;

import org.application.decoders.VignereCipher;
import org.crypography_tools.Tools;

import javax.swing.*;
import java.util.Arrays;

public class Main {

    public static DialogueBoxHandler boxHandler;

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException {
        System.out.println("1");

        boxHandler = new DialogueBoxHandler();

        boxHandler.OpenCipherDialogue();

    }

    public static void OnCipherTextInput(String ciphertext) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        String f_text = Tools.Format(ciphertext);

        String[] cosets = Tools.MakeCosets(f_text, 3);

        for (String coset : cosets) {
            System.out.println(coset);
        }

        System.out.println(VignereCipher.GetKeyword(f_text));

        boxHandler.OpenVignereOutput(3, "cheese", "cheese yet again");

    }
}