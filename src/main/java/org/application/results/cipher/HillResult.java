package org.application.results.cipher;

import org.application.decoders.HillCipher;
import org.application.results.CipherResult;
import org.crypography_tools.Callback;

public class HillResult extends CipherResult<double[][], HillResult> {

    public double[][] thhe;

    public void ReAnalyse() {
        callback.execute(this);
    }

    public HillResult(String ciphertext, String plaintext, double[][] keyword, double[][] thhe, Callback<HillResult> callback) {
        super(ciphertext, plaintext, keyword, callback);
        this.thhe = thhe;
    }
}
