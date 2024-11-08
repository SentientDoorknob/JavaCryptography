package org.crypography_tools;

import org.application.results.CipherResult;

public interface Callback<ChildType> {
    void execute(ChildType result);
}
