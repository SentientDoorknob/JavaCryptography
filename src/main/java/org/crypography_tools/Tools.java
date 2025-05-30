package org.crypography_tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Tools {

    public static char[] Alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    public static double EnglishIOC = 0.0667;

    public static final double[] EnglishLetterFrequencies = {0.12, 0.091, 0.0812, 0.0768, 0.0731, 0.0695, 0.0628, 0.0602, 0.0592, 0.0432,
            0.0398, 0.0288, 0.0271, 0.0261, 0.023, 0.0211, 0.0209, 0.0203, 0.0182, 0.0149,
            0.0111, 0.0069, 0.0017, 0.0011, 0.001, 0.0007};

    public static final String[][] PolybiusDefault = {{"a", "b", "c", "d", "e"},
                                        {"f", "g", "h", "i", "j"},
                                        {"k", "l", "m", "n", "o"},
                                        {"p", "q", "r", "s", "t"},
                                        {"u", "v", "w", "x", "y"}};



    public static String ApplyToLetters(String input, LambdaChar app) {
        return input.chars()
                .filter(Character::isLetter)
                .mapToObj(c -> (char) c)
                .map(app::ly)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public static String Format(String text) {
        LambdaChar filter = (c) -> Character.isLetter(c) ? Character.toLowerCase(c) : 'q';
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
        int[][] cosets = new int[num][(int) text.length / num];

        for (int i = 0; i < text.length; i++) {
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

    public static int[] InterleaveIntArray(int[][] array) {
        ArrayList<Integer> list = new ArrayList<>();

        int i = 0;
        boolean cont = true;

        while (cont) {
            cont = false; // Assume this will be the last iteration unless proven otherwise
            for (int[] set : array) {
                if (i < set.length) {
                    list.add(set[i]);
                    cont = true;
                }
            }
            i++;
        }

        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    public static int ModularInverse(int dividend, int divisor) {
        for (int X = 1; X < divisor; X++)
            if (((dividend % divisor) * (X % divisor)) % divisor == 1)
                return X;
        return 1;
    }

    public static double[] GetColumn(double[][] matrix, int column) {
        return Arrays.stream(matrix).mapToDouble(doubles -> doubles[column]).toArray();
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
        int numDigraphs = text.length() / 2;

        String[] digraphs = new String[numDigraphs];

        for (int i = 0; i < text.length(); i += 2) {
            digraphs[i / 2] = "" + chars[i] + chars[i + 1];
        }

        return digraphs;
    }

    public static void DisplayHashmap(HashMap<String, Integer> map) {
        map.entrySet()
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

    public static int[] GetSpan(boolean[] bools) {
        int min = 10;
        int max = -1;

        for (int i = 1; i < 11; i++) {
            if (!bools[i % 10]) continue;
            if (i < min) min = i;
            if (i > max) max = i;
        }

        int span_length = max - min + 1;
        min = min % 10;

        return new int[] {min, span_length};
    }

    public static <T> T[][] LLtoAA(ArrayList<ArrayList<T>> l, Class<T> clazz) {
        T[][] array = (T[][]) java.lang.reflect.Array.newInstance(clazz, l.size(), 0);

        for (int i = 0; i < l.size(); i++) {
            array[i] = l.get(i).toArray((T[]) java.lang.reflect.Array.newInstance(clazz, l.get(i).size()));
        }

        return array;
    }

    public static int[] StringToIntArray(String[] s) {
        int[] result = new int[s.length];
        for (int i = 0; i < s.length; i++) {
            result[i] = Integer.parseInt(s[i]);
        }
        return result;
    }

    public static double XSquared(double[] observed, double[] expected) {
        int length = Math.min(observed.length, expected.length);

        double sum = 0;
        for (int i = 0; i < length; i++) {
            sum += (observed[i] - expected[i]) * (observed[i] - expected[i]) / expected[i];
        }

        return sum;
    }

    public static double XSquaredEnglish(String text) {
        double[] observed = DecimalFrequency(text);
        return XSquared(observed, EnglishLetterFrequencies);
    }

    public static int EvaluateTableComponent(boolean[][] comp) {
        int score = 0;
        for (boolean[] coset : comp) {
            for (int i = 0; i < 10; i++) {
                int prev = Math.floorMod(i - 1, 10);
                int next = Math.floorMod(i + 1, 10);

                if (!coset[i]) {score++; continue;}

                if (!coset[prev] || !coset[next]) score++;
            }
        }

        return score;
    }

    public static int[] GetSpanStartingPossibilities(int[] span) {
        if (span[1] >= 5) return new int[] {span[0] - 1};

        int variance = 5 - span[1];
        ArrayList<Integer> points = new ArrayList<>();

        for (int i = 0; i < variance + 1; i++) {
            int point = span[0] - i - 1;
            if (point < 2) break;
            points.add(point);
        }

        return points.stream().mapToInt(Integer::intValue).toArray();
    }

    public static Stream<int[]> CartesianProduct(int[][] sets, int index) {
        if (index == sets.length) {
            return Stream.of(new int[0]);
        }

        return Arrays.stream(sets[index]).boxed().flatMap(element ->
                CartesianProduct(sets, index + 1).map(rest -> {
                    int[] result = new int[rest.length + 1];
                    result[0] = element;
                    System.arraycopy(rest, 0, result, 1, rest.length);
                    return result;
                })
        );
    }
}
