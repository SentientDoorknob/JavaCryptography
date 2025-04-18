package org.application.results.cipher;

import org.application.results.CipherResult;
import org.crypography_tools.Callback;

public class NihilistResult extends CipherResult<int[], NihilistResult> {
    public void ReAnalyse() {
        callback.execute(this);
    }

    public double IoC;
    public int[][] possibleKeywords;

    public NihilistResult(String ciphertext, String plaintext, int[][] _possibleKeywords, int[] keyword, double ioc, Callback<NihilistResult> callback) {
        super(ciphertext, plaintext, keyword, callback);
        IoC = ioc;
        possibleKeywords = _possibleKeywords;
    }
}
