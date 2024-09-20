package org.crypography_tools;

public class Tools {

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

    public static String[] MakeCosets(String text, int num) {
        String[] cosets = new String[num];

    }
}
