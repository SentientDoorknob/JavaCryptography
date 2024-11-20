package org.application.results.cipher;

import org.application.decoders.BifidCipher;
import org.application.results.ConversionResult;
import org.crypography_tools.Callback;

public class BifidResult extends ConversionResult<BifidResult> {
    public BifidResult(String ciphertext, String conversiontext) {
        super(ciphertext, conversiontext);
    }
}
