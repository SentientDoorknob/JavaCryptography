package org.application.results.cipher;

import org.application.results.CipherResult;
import org.crypography_tools.Callback;

public class AffineResult extends CipherResult<int[], AffineResult> {
    public int[] ET;

    @Override
    public void ReAnalyse() {
        callback.execute(this);
    }

    public AffineResult(String ciphertext, String plaintext, int[] keyword, Callback<AffineResult> callback, int[] ET) {
        super(ciphertext, plaintext, keyword, callback);
        this.ET = ET;
    }
}
