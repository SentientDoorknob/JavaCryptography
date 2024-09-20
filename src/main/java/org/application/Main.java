package org.application;

import org.crypography_tools.Tools;

import javax.swing.*;
import java.text.Format;

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
        System.out.println(ciphertext);
        System.out.println(Tools.Format(ciphertext));

    }
}