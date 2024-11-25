package org.application.results.cipher;

import org.application.results.CipherResult;
import org.crypography_tools.Callback;

public class NihilistResult extends CipherResult<int[], NihilistResult> {
    public NihilistResult(String ciphertext, String plaintext, int[] keyword, Callback<NihilistResult> callback) {
        super(ciphertext, plaintext, keyword, callback);
        // no.
    }
}
