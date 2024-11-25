package org.application.results.cipher;

import org.application.results.CipherResult;
import org.crypography_tools.Callback;

public class VignereResult extends CipherResult<String, VignereResult> {

    public void ReAnalyse() {
        callback.execute(this);
    }

    public VignereResult(String ciphertext, String plaintext, String keyword, Callback<VignereResult> callback) {
        super(ciphertext, plaintext, keyword, callback);
    }
}
