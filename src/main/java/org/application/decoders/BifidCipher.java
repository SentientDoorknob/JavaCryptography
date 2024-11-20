package org.application.decoders;

import org.application.Main;
import org.application.results.cipher.BifidResult;
import org.crypography_tools.Tools;

/*

    Method Source: https://www.dcode.fr/bifid-cipher
    SUBTEXT -> Text for substitution.

 */

public class BifidCipher {

    // Returns SUBTEXT. Uses default bifid grid in tools for decryption.
    public static String ToSubstitutionCipher(String ciphertext) {
        int[] ints = ciphertext.chars().map(x -> x - '0' - 1).toArray();

        String substitution = "";

        for (int i = 0; i < ciphertext.length(); i += 2) {
            substitution += Tools.PolybiusDefault[ints[i]][ints[i+1]];
        }

        System.out.println(Tools.IndexOfCoincidence(substitution));

        return substitution;
    }

    public static void ConvertWithResultsDialogue(String ciphertext) {
        String substitutionText = ToSubstitutionCipher(ciphertext);
        Main.boxHandler.OpenBifidOutput(new BifidResult(ciphertext, substitutionText));
    }
}
