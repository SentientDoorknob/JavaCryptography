package org.application.results;

import org.crypography_tools.Callback;

public abstract class CipherResult<KeywordType, ChildType> extends ConversionResult<ChildType> {
    public String ciphertext;
    public String plaintext;

    public KeywordType predictedKeyword;
    public KeywordType usedKeyword;

    public Callback<ChildType> callback;

    public void ReAnalyse() {}

    public CipherResult(String ciphertext, String plaintext, KeywordType keyword, Callback<ChildType> callback) {
        super(ciphertext, plaintext);
        this.predictedKeyword = keyword;
        this.usedKeyword = keyword;
        this.callback = callback;
    }
}
