package com.cwunder.recipe._shared;

import java.util.HashMap;
import java.util.Map;
import static java.util.Map.entry;
import java.util.stream.Stream;

public class CaptchaErrorException extends RuntimeException {
    private final Map<String, String> errorCodeLookupMap = Map.ofEntries(
            entry("missing-input-secret", "Serverside error"),
            entry("invalid-input-secret", "Serverside error"),
            entry("missing-input-response", "Serverside error"),
            entry("invalid-input-response", "Invalid g-recaptcha-response"),
            entry("bad-request", "Serverside error"),
            entry("timeout-or-duplicate",
                    "Invalid g-recaptcha-response: either is too old or has been used previously."));

    private final String[] errorCodes;
    private final Map<String, String> errorCodeMap = new HashMap<>();

    public CaptchaErrorException(String entityName, String[] errorCodes) {
        super(entityName);
        this.errorCodes = errorCodes;
        if (errorCodes == null)
            return;
        Stream.of(this.errorCodes).forEach(errCode -> {
            var errDescr = errorCodeLookupMap.get(errCode);
            if (errDescr != null) {
                errorCodeMap.put(errCode, errDescr);
            }
        });
    }

    public Map<String, String> getErrorCodeMap() {
        return errorCodeMap;
    }
}