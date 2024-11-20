package org.application.results;

public abstract class ConversionResult<ChildType> {
    public String ciphertext;
    public String plaintext;

    public ConversionResult(String ciphertext, String conversiontext) {
        this.ciphertext = ciphertext;
        this.plaintext = conversiontext;
    }
}
