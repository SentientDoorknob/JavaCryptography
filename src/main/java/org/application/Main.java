package org.application;

import org.crypography_tools.Tools;

import javax.swing.*;
import java.util.Arrays;

public class Main {

    public static DialogueBoxHandler boxHandler;

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException {
        System.out.println("1");

        boxHandler = new DialogueBoxHandler();

        boxHandler.OpenCipherDialogue();

        System.out.println("2");
    }

    public static void OnCipherTextInput(String ciphertext) {
        System.out.println("3");

        String f_text = Tools.Format(ciphertext);

        String[] cosets = Tools.MakeCosets(f_text, 3);

        for (String coset : cosets) {
            System.out.println(coset);
        }

        System.out.println(Arrays.toString(Tools.AbsoluteFrequency(f_text)));
        System.out.println(Arrays.toString(Tools.DecimalFrequency(f_text)));

    }
}