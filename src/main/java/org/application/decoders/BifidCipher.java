package org.application.decoders;

import org.application.Main;
import org.crypography_tools.Tools;

public class BifidCipher {

    public static String ToSubstitutionCipher(String ciphertext) {
        int[] ints = ciphertext.chars().map(x -> x - '0' - 1).toArray();

        String substitution = "";

        for (int i = 0; i < ciphertext.length(); i += 2) {
            substitution += Tools.Bifid[ints[i]][ints[i+1]];
        }

        System.out.println(Tools.IndexOfCoincidence(substitution));

        return substitution;
    }

    public static void ConvertWithResultsDialogue(String ciphertext) {
        String substitutionText = ToSubstitutionCipher(ciphertext);
        Main.boxHandler.OpenBifidOutput(substitutionText);
    }

}
