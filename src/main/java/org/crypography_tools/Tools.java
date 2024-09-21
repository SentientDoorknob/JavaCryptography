package org.crypography_tools;

import java.util.Arrays;

public class Tools {

    public static char[] Alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();


    public static String ApplyToLetters(String input, LambdaChar app) {
        return input.chars()
                .filter(c -> Character.isLetter(c))
                .mapToObj(c -> (char) c)
                .map(c -> app.ly(c))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public static String Format(String text) {
        LambdaChar filter = (c) -> Character.isLetter(c) ? Character.toLowerCase(c) : null;
        return ApplyToLetters(text, filter);
    }

    public static int ToAlphabetIndex(char c) {
        return (int)c - 65;
    }

    public static String[] MakeCosets(String text, int num) {
        String[] cosets = new String[num];

        Arrays.fill(cosets, "");

        for (int i = 0; i < text.length(); i++) {
            cosets[i % num] += text.charAt(i);
        }

        return cosets;
    }

    public static int[] AbsouluteFrequency(String text) {

        int[] output = new int[26];

        for (char c : text.toCharArray()) {
            output[ToAlphabetIndex(c)] += 1;
        }

        return output;
    }

    public static double[] DecimalFrequency(String text) {
        int[] absoluteFrequencies = AbsouluteFrequency(text);

        return Arrays.stream(absoluteFrequencies)
                .mapToDouble(i -> (double) i / text.length())
                .toArray();
    }

    public static double IndexOfCoincidence(String text) {

        int text_length = text.length();
        double length_multiplier = 1 / (text_length * (text_length - 1) / 2);


    }
}
