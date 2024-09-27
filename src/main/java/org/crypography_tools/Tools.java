package org.crypography_tools;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Tools {

    public static char[] Alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    public static double EnglishIOC = 0.0667;


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
        return (int) c - 97;
    }

    public static String[] MakeCosets(String text, int num) {
        String[] cosets = new String[num];

        Arrays.fill(cosets, "");

        for (int i = 0; i < text.length(); i++) {
            cosets[i % num] += text.charAt(i);
        }

        return cosets;
    }

    public static int[] AbsoluteFrequency(String text) {

        int[] output = new int[26];

        for (char c : text.toCharArray()) {
            output[ToAlphabetIndex(c)] += 1;
        }

        return output;
    }

    public static double[] DecimalFrequency(String text) {
        int[] absoluteFrequencies = AbsoluteFrequency(text);

        return Arrays.stream(absoluteFrequencies)
                .mapToDouble(i -> (double) i / text.length())
                .toArray();
    }

    public static double IndexOfCoincidence(String text) {

        int text_length = text.length();
        double lengthMultiplier = (text_length * (text_length - 1));

        int[] absoluteFrequencies = AbsoluteFrequency(text);

        double sum = 0;
        for (int frequency : absoluteFrequencies) {
            sum += frequency * (frequency - 1);
        }

        return sum / lengthMultiplier;
    }

    public static String ShiftLetters(String letters, int by) {
        return letters.chars()
                .map(x -> Math.floorMod(x - 'a' + by, 26) + 'a')
                .mapToObj(x -> (char) x)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    public static String Interleave(String[] cosets) {
        StringBuilder output = new StringBuilder(); // Use StringBuilder for better performance

        int i = 0;
        boolean cont = true;

        while (cont) {
            cont = false; // Assume this will be the last iteration unless proven otherwise
            for (String set : cosets) {
                if (i < set.length()) {
                    output.append(set.charAt(i));
                    cont = true;
                }
            }
            i++;
        }

        return output.toString();
    }

    public static int ModularInverse(int dividend, int divisor) {
        for (int X = 1; X < divisor; X++)
            if (((dividend % divisor) * (X % divisor)) % divisor == 1)
                return X;
        return 1;
    }
}
