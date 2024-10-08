package org.crypography_tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Collections.sort;

public class Tools {

    public static char[] Alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    public static double EnglishIOC = 0.0667;

    public static String[][] PolybiusDefault = {{"a", "b", "c", "d", "e"},
                                        {"f", "g", "h", "i", "j"},
                                        {"k", "l", "m", "n", "o"},
                                        {"p", "q", "r", "s", "t"},
                                        {"u", "v", "w", "x", "y"}};



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

    public static String DigitFormat(String text) {
        return text.replaceAll("[^0-9]", "");
    }

    public static String NumberFormat(String text) {
        return text.replaceAll("\n", " ").replaceAll("[^0-9 ]", "");
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

    public static int[][] MakeIntArrayCosets(int[] text, int num) {
        int[][] cosets = new int[num][text.length / num - num];

        for (int i = 0; i < text.length - num; i++) {
            try {cosets[i % num][i / num] = text[i]; }
            catch (IndexOutOfBoundsException ignored) {}
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

    public static double[] GetColumn(double[][] matrix, int column) {
        return IntStream.range(0, matrix.length)
                .mapToDouble(i -> matrix[i][column]).toArray();
    }

    public static int[] ReducePermutationKeyword(int[] keyword) {

        Integer[] indices = new Integer[keyword.length];
        for (int i = 0; i < keyword.length; i++) {
            indices[i] = i;
        }

        Arrays.sort(indices, Comparator.comparingInt(i -> keyword[i]));

        int[] result = new int[keyword.length];

        for (int rank = 0; rank < indices.length; rank++) {
            result[indices[rank]] = rank;
        }

        return result;
    }

    public static HashMap<String, Integer> Count(String[] things) {
        HashMap<String, Integer> dictionary = new HashMap<>();

        for (String thing : things) {
            dictionary.put(thing, dictionary.getOrDefault(thing, 0) + 1);
        }

        return dictionary;
    }

    public static String[] SplitDigraphs(String text) {
        char[] chars = text.toCharArray();
        int numDigraphs = (int) text.length() / 2;

        String[] digraphs = new String[numDigraphs];

        for (int i = 0; i < text.length(); i += 2) {
            digraphs[i / 2] = "" + chars[i] + chars[i + 1];
        }

        return digraphs;
    }

    public static void DisplayHashmap(HashMap<String, Integer> map) {
        map.entrySet().stream()
                .forEach(entry -> System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue()));
    }

    public static ArrayList<Integer> GetTrues(boolean[] array) {
        ArrayList<Integer> trues = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            if (array[i]) {
                trues.add(i);
            }
        }

        return trues;
    }

    public static int[] GetMaximumSpan(boolean[] bools) {
        int[] maxSpan = new int[2];
        // span, start

        int[] tempSpan = new int[2];
        for (int i = 0; i < bools.length; i++) {
            boolean b = bools[i];

            if (!b) {
                tempSpan = new int[] {0, 0};
                continue;
            }

            if (Arrays.equals(tempSpan, new int[]{0, 0})) {
                tempSpan[0] = 1;
                tempSpan[1] = i;
                continue;
            }

            tempSpan[0]++;

            if (tempSpan[0] > maxSpan[0]) {
                maxSpan = tempSpan.clone();
            }
        }

        return maxSpan;
    }

    public static <T> T[][] LLtoAA(ArrayList<ArrayList<T>> l, Class<T> clazz) {
        T[][] array = (T[][]) java.lang.reflect.Array.newInstance(clazz, l.size(), 0);

        for (int i = 0; i < l.size(); i++) {
            array[i] = l.get(i).toArray((T[]) java.lang.reflect.Array.newInstance(clazz, l.get(i).size()));
        }

        return array;
    }

}
