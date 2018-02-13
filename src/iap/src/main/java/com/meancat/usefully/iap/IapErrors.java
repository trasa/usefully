package com.meancat.usefully.iap;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class IapErrors {

    private Map<String, Boolean> errorTypes = ImmutableMap.<String, Boolean>builder()
            // Retries on these will always fail:
            .put("DECODE_FAILED", false)
            .put("BAD_BID", false)
            .put("RETRIEVED_BID_MISMATCH", false)
            .put("RETRIEVED_PLAYER_MISMATCH", false)
            .put("NOT_VALID", false)
            .put("RECEIPT_NOT_FOUND", false)
            .put("BAD_PLAYER_ID", false)
            .put("ALREADY_CONSUMED", false)

            // these are OK for retry:
            .put("VERIFY_EXCEPTION", true)

            .build();


    public boolean canRetry(String errorType) {
        Boolean retryable = errorTypes.get(errorType);
        return retryable == null || retryable;
    }
}
