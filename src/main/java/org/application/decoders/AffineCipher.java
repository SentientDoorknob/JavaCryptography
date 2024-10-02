package org.application.decoders;

import org.application.Main;
import org.crypography_tools.Tools;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class AffineCipher {
    public static int[] GetAffineKey(String ciphertext) {
        int[] sortedFrequencies = Tools.AbsoluteFrequency(ciphertext);
        int[] absoluteFrequencies = sortedFrequencies.clone();

        Arrays.sort(sortedFrequencies);

        int e_index = Arrays.stream(absoluteFrequencies).boxed().collect(Collectors.toList()).indexOf(sortedFrequencies[25]);
        int t_index = Arrays.stream(absoluteFrequencies).boxed().collect(Collectors.toList()).indexOf(sortedFrequencies[24]);

        // e_index is index of the equivalent of e
        // therefore, m(E) + b = e_index mod 26
        // = 5m + b = e_index
        // AND 20m + b = t_index

        // 5m + b = e_index   mod 26
        // 20m + b = t_index  mod 26

        // subtracting,
        // 15m = t_index - e_index
        // m = (t_index - e_index) * 7 mod 26
        int m = Math.floorMod((t_index - e_index) * 7, 26);

        // b = e_index - 5m
        int b = Math.floorMod(e_index - 4 * m, 26);

        return new int[] {m, b};
    }

    public static String DecryptWithKey(String ciphertext, int[] key) {
        System.out.println(Arrays.toString(key));
        int m = key[0];
        int b = key[1];

        return ciphertext
                .chars()
                .map(x -> Math.floorMod((x - 'a' - b) * Tools.ModularInverse(m, 26), 26))  // Apply affine cipher
                .map(x -> x + 'a')  // Convert back to character
                .mapToObj(c -> String.valueOf((char) c))  // Convert to String
                .collect(Collectors.joining());
    }

    public static int[] GetKeyWithLetters(int e, int t) {
        int m = Math.floorMod((t - e) * 7, 26);

        // b = e_index - 5m
        int b = Math.floorMod(e - 4 * m, 26);

        return new int[] {m, b};
    }

    public static void DecryptWithResultsDialogue(String ciphertext) {
        int[] key = GetAffineKey(ciphertext);
        String plaintext = DecryptWithKey(ciphertext, key);

        Main.boxHandler.OpenAffineOutput(key, key, plaintext, ciphertext);
    }

    public static void DecryptFromDialogue(String ciphertext, String[] et, int[] predictedKey) {
        int[] key = GetKeyWithLetters(et[0].charAt(0) - 'a', et[1].charAt(0) - 'a');
        String plaintext = DecryptWithKey(ciphertext, key);

        Main.boxHandler.OpenAffineOutput(predictedKey, key, plaintext, ciphertext);
    }


}
