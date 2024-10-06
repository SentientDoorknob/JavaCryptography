package org.application.decoders;

import java.util.Arrays;

public class NihilistCipher {
    public static void GetCosetTable(String ciphertext, int length) {
        String[] coordinates = (String[]) Arrays.stream(ciphertext.split(" ")).map(x -> x.toString()).toArray();
    }
}
