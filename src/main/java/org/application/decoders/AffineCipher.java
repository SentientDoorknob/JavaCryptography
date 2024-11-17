package org.application.decoders;

import org.application.Main;
import org.application.results.cipher.AffineResult;
import org.crypography_tools.Tools;

import java.util.Arrays;
import java.util.stream.Collectors;

/*

    Method Source: https://math.asu.edu/sites/default/files/affine.pdf
    ET  -> int[] (index of e, index of t)
    KEY -> int[] (m, b)

 */

public class AffineCipher {

    // CIPHERTEXT -> ET -> KEY -> PLAINTEXT -> RESULT

    // Returns ET. Uses 2 highest frequency letters for E and T respectively.
    public static int[] GetET(String ciphertext) {
        int[] sortedFrequencies = Tools.AbsoluteFrequency(ciphertext);
        int[] absoluteFrequencies = sortedFrequencies.clone();

        Arrays.sort(sortedFrequencies);

        int e_index = Arrays.stream(absoluteFrequencies).boxed().toList().indexOf(sortedFrequencies[25]);
        int t_index = Arrays.stream(absoluteFrequencies).boxed().toList().indexOf(sortedFrequencies[24]);

        return new int[] {e_index, t_index};
    }

    // Returns KEY. Given ET and follows method source.
    public static int[] GetKeyWithET(int[] ET) {

        int e = ET[0];
        int t = ET[1];

        int m = Math.floorMod((t - e) * 7, 26);

        // b = e_index - 5m
        int b = Math.floorMod(e - 4 * m, 26);

        return new int[] {m, b};
    }

    // Returns PLAINTEXT. Applies x = (y - b) / m to all ciphertext.
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


    public static void DecryptWithResultsDialogue(String ciphertext) {
        int[] ET = GetET(ciphertext);

        int[] key = GetKeyWithET(ET);
        String plaintext = DecryptWithKey(ciphertext, key);

        AffineResult result = new AffineResult(ciphertext, plaintext, key, AffineCipher::DecryptFromDialogue, ET);

        Main.boxHandler.OpenAffineOutput(result);
    }

    public static void DecryptFromDialogue(AffineResult result) {
        System.out.println(Arrays.toString(result.ET));
        int[] key = GetKeyWithET(result.ET);
        String plaintext = DecryptWithKey(result.ciphertext, key);

        result.usedKeyword = key;
        result.plaintext = plaintext;

        Main.boxHandler.OpenAffineOutput(result);
    }


}
