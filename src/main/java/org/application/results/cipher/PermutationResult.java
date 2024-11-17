package org.application.results.cipher;

import org.application.results.CipherResult;
import org.crypography_tools.Callback;

public class PermutationResult extends CipherResult<int[], PermutationResult> {
    public PermutationResult(String ciphertext, String plaintext, int[] keyword, Callback<PermutationResult> callback) {
        super(ciphertext, plaintext, keyword, callback);
    }
}
