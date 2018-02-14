package com.meancat.usefully.iap.google;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meancat.usefully.util.Base64;

public class GooglePlayStore {
    private ObjectMapper objectMapper;

    public GooglePlayStore(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public GooglePlayReceipt decodeTicket(String rawReceipt) throws IOException {
        return objectMapper.readValue(Base64.decodeFast(rawReceipt), GooglePlayReceipt.class);
    }
}
