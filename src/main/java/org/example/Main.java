package org.example;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException {
        System.out.println("Hello world!");

        DialogueBoxHandler boxHandler = new DialogueBoxHandler();

        String ciphertext = boxHandler.GetCipherText();

        System.out.println(ciphertext);
    }
}