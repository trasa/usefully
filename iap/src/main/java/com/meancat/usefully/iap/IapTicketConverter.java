package com.meancat.usefully.iap;

import static com.google.common.collect.Maps.newHashMap;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.meancat.usefully.util.Base64;

public class IapTicketConverter {
    private static final Logger logger = LoggerFactory.getLogger(IapTicketConverter.class);

    private ObjectMapper jsonObjectMapper;

    public IapTicketConverter(ObjectMapper jsonObjectMapper) {
        this.jsonObjectMapper = jsonObjectMapper;
    }


    /**
     * Sadly, decodedTicket is not valid json, but instead is a P-List, a twisted and
     * evil text encoding.
     *
     * Turn it into valid json.
     *
     * @param decodedTicket Apple property-list ticket
     * @return Json ticket
     */
    public String convertToJson(String decodedTicket) {
        if (Strings.isNullOrEmpty(decodedTicket))
            return decodedTicket;

        char[] buf = decodedTicket.toCharArray();
        ParseState state = new ParseState();
        for (int i = 0; i < buf.length; i++) {
            convertChar(buf, i, state);
        }
        // there's a final trailing "," from the last ; that we need to get rid of.
        removeTrailingComma(buf);
        return new String(buf);
    }


    private void convertChar(char[] buf, int i, ParseState state) {
        char c = buf[i];

        if (state.isEscapedState) {
            // ignore this character completely
            state.isEscapedState = false;
            return;
        }
        switch(c) {
            case '=':
                if (!state.isInString) {
                    // convert = to :
                    buf[i] = ':';
                }
                break;
            case ';':
                if (!state.isInString) {
                    // convert ; to ,
                    buf[i] = ',';
                }
                break;
            case '"':
                // entering or leaving a string -
                // ignore contents of the string.
                state.isInString = !state.isInString;
                break;
            case '\\':
                // found a first \ , we'll ignore the following character (above)
                state.isEscapedState = true;
                break;
        }
    }


    private void removeTrailingComma(char[] buf) {
        boolean allDone = false;
        for(int i=buf.length - 1; i >= 0; i--) {
            char c = buf[i];

            switch(c) {
                case ',':
                    // found it, we're done here.
                    buf[i] = ' ';
                    allDone = true;
                    break;
                case '}':
                    // keep looking
                    break;
                default:
                    if (Character.isWhitespace(c)) {
                        // keep looking
                        break;
                    }
                    // found something that isn't what we're looking for -
                    // no trailing "," found.
                    logger.debug("found a '{}' {}", c, (int)c);
                    allDone = true;
                    break;
            }
            if (allDone)
                break;
        }
    }

    @SuppressWarnings("unchecked")
    public IapTicket readTicket(String decodedTicket) throws IOException {
        if (decodedTicket == null) {
            throw new IllegalArgumentException("decodedTicket must not be null");
        }
        IapTicket ticket = jsonObjectMapper.readValue(convertToJson(decodedTicket), IapTicket.class);
        String decodedPurchaseInfo = new String(Base64.decodeFast(ticket.rawPurchaseInfo),"UTF-8");
        logger.debug("decodedPurchaseInfo: {}", decodedPurchaseInfo);
        //noinspection unchecked
        ticket.purchaseInfo = jsonObjectMapper.readValue(convertToJson(decodedPurchaseInfo), Map.class);
        fixPurchaseInfoKeys(ticket.purchaseInfo);
        return ticket;
    }

    /**
     * The purchaseInfo fields decoded use dashes (-) instead of _ in their keys,
     * but IapReceipts use underscores. So this duplicates the fields replacing _ with -.
     *
     * @param purchaseInfo map that is modified by duplicating values
     */
    void fixPurchaseInfoKeys(Map<String, Object> purchaseInfo) {
        Map<String, Object> duplicates = newHashMap();
        for(Map.Entry<String, Object> entry : purchaseInfo.entrySet()) {
            duplicates.put(entry.getKey().replace('-', '_'), entry.getValue());
        }
        purchaseInfo.putAll(duplicates);
    }

    public String decode(String ticket) throws IOException {
        if (ticket == null) {
            throw new IllegalArgumentException("ticket must not be null");
        }
        String decoded = new String(Base64.decodeFast(ticket), "UTF-8");
        logger.debug("Decoded ticket: {}", decoded);
        return decoded;
    }


    private class ParseState {
        public boolean isEscapedState;
        public boolean isInString;
    }
}
