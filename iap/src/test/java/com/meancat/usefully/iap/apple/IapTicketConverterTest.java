package com.meancat.usefully.iap.apple;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class IapTicketConverterTest {
    private static final Logger logger = LoggerFactory.getLogger(IapTicketConverterTest.class);
    private ObjectMapper jsonObjectMapper;
    private IapTicketConverter converter;

    @Before
    public void setUp() {
        jsonObjectMapper = new ObjectMapper(new JsonFactory());
        jsonObjectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        converter = new IapTicketConverter(jsonObjectMapper);
    }

    @Test
    public void appleToJson_Simple() throws IOException {
        String s = "{ \"hello\" = \"world!\"; }";

        String json = converter.convertToJson(s);

        logger.debug(json);
        assertEquals("{ \"hello\" : \"world!\"  }", json);
        // is it valid json?
        Map map = jsonObjectMapper.readValue(json, Map.class);
        assertEquals("world!", map.get("hello"));
    }

    @Test
    public void appleToJson_EscapeCharacters_InString() throws IOException {
        String s = "{ \"he\\\\llo\" = \"world!\"; }";

        String json = converter.convertToJson(s);

        logger.debug(json);
        assertEquals("{ \"he\\\\llo\" : \"world!\"  }", json);
        // is it valid json?
        Map map = jsonObjectMapper.readValue(json, Map.class);
        assertEquals("world!", map.get("he\\llo"));
    }

    @Test
    public void appleToJson_EscapeCharacters_OutsideString() throws IOException {
        String s = "{\t\n \"hello\" = \"world!\"; }";

        String json = converter.convertToJson(s);

        logger.debug(json);
        assertEquals("{\t\n \"hello\" : \"world!\"  }", json);
        // is it valid json?
        Map map = jsonObjectMapper.readValue(json, Map.class);
        assertEquals("world!", map.get("hello"));
    }

    @Test
    public void appleToJson_EscapeCharacters_OutsideString_AtEnd() throws IOException {
        String s = "{ \"hello\" = \"world!\"; \t\n}";

        String json = converter.convertToJson(s);

        logger.debug(json);
        assertEquals("{ \"hello\" : \"world!\"  \t\n}", json);
        // is it valid json?
        Map map = jsonObjectMapper.readValue(json, Map.class);
        assertEquals("world!", map.get("hello"));
    }

    @Test
    public void appleToJson_withoutExtraSemiAtEnd() throws IOException {
        String s = "{ \"hello\" = \"world!\" }";

        String json = converter.convertToJson(s);

        logger.debug(json);
        assertEquals("{ \"hello\" : \"world!\" }", json);
        // is it valid json?
        Map map = jsonObjectMapper.readValue(json, Map.class);
        assertEquals("world!", map.get("hello"));
    }



    @Test
    public void convertValidJson() throws IOException {
        IapTicket ticket = converter.readTicket("{ " +
                "\"signature\" = \"AnaLPQk0GUYTKZneEE9I6f41TPkGKnrlLddPhoVRwg+PWa6XdJTgh66D1t2S4XJSpmikXjbckz/rxZGuYsP9CwEgmc7NRj2wA5xefmBKPIyLtfMxTRY5AnfewSAlzYuXqcv+anvP+Vwvi9Jl3iEKFOrjNB4ICw8+5BejZNtRbEoUAAADVzCCA1MwggI7oAMCAQICCGUUkU3ZWAS1MA0GCSqGSIb3DQEBBQUAMH8xCzAJBgNVBAYTAlVTMRMwEQYDVQQKDApBcHBsZSBJbmMuMSYwJAYDVQQLDB1BcHBsZSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTEzMDEGA1UEAwwqQXBwbGUgaVR1bmVzIFN0b3JlIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MB4XDTA5MDYxNTIyMDU1NloXDTE0MDYxNDIyMDU1NlowZDEjMCEGA1UEAwwaUHVyY2hhc2VSZWNlaXB0Q2VydGlmaWNhdGUxGzAZBgNVBAsMEkFwcGxlIGlUdW5lcyBTdG9yZTETMBEGA1UECgwKQXBwbGUgSW5jLjELMAkGA1UEBhMCVVMwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAMrRjF2ct4IrSdiTChaI0g8pwv/cmHs8p/RwV/rt/91XKVhNl4XIBimKjQQNfgHsDs6yju++DrKJE7uKsphMddKYfFE5rGXsAdBEjBwRIxexTevx3HLEFGAt1moKx509dhxtiIdDgJv2YaVs49B0uJvNdy6SMqNNLHsDLzDS9oZHAgMBAAGjcjBwMAwGA1UdEwEB/wQCMAAwHwYDVR0jBBgwFoAUNh3o4p2C0gEYtTJrDtdDC5FYQzowDgYDVR0PAQH/BAQDAgeAMB0GA1UdDgQWBBSpg4PyGUjFPhJXCBTMzaN+mV8k9TAQBgoqhkiG92NkBgUBBAIFADANBgkqhkiG9w0BAQUFAAOCAQEAEaSbPjtmN4C/IB3QEpK32RxacCDXdVXAeVReS5FaZxc+t88pQP93BiAxvdW/3eTSMGY5FbeAYL3etqP5gm8wrFojX0ikyVRStQ+/AQ0KEjtqB07kLs9QUe8czR8UGfdM1EumV/UgvDd4NwNYxLQMg4WTQfgkQQVy8GXZwVHgbE/UC6Y7053pGXBk51NPM3woxhd3gSRLvXj+loHsStcTEqe9pBDpmG5+sk4tw+GK3GMeEN5/+e1QT9np/Kl1nj+aBw7C0xsy0bFnaAd1cSS6xdory/CUvM6gtKsmnOOdqTesbp0bs8sn6Wqs0C9dgcxRHuOMZ2tm8npLUm7argOSzQ==\";" +
                "\"purchase-info\" = \"ewoJIm9yaWdpbmFsLXB1cmNoYXNlLWRhdGUtcHN0IiA9ICIyMDEyLTA5LTE5IDEzOjEwOjM0IEFtZXJpY2EvTG9zX0FuZ2VsZXMiOwoJInVuaXF1ZS1pZGVudGlmaWVyIiA9ICJhMWFiYTY2ZDg3Yjg1NDJjY2ZiYTlmZjI3YmQ5NDEyMzhiMmQwZGQwIjsKCSJvcmlnaW5hbC10cmFuc2FjdGlvbi1pZCIgPSAiMTAwMDAwMDA1NjExNzAxOCI7CgkiYnZycyIgPSAiMC4xLjAiOwoJInRyYW5zYWN0aW9uLWlkIiA9ICIxMDAwMDAwMDU2MTE3MDE4IjsKCSJxdWFudGl0eSIgPSAiMSI7Cgkib3JpZ2luYWwtcHVyY2hhc2UtZGF0ZS1tcyIgPSAiMTM0ODA4NTQzNDk1NSI7CgkicHJvZHVjdC1pZCIgPSAiY29tLmdsdS5kcmFnb25zdG9ybS5DUk9XTlNfUEFDS19GIjsKCSJpdGVtLWlkIiA9ICI1MzYzNjY4NzQiOwoJImJpZCIgPSAiY29tLmdsdS5kcmFnb25zdG9ybSI7CgkicHVyY2hhc2UtZGF0ZS1tcyIgPSAiMTM0ODA4NTQzNDk1NSI7CgkicHVyY2hhc2UtZGF0ZSIgPSAiMjAxMi0wOS0xOSAyMDoxMDozNCBFdGMvR01UIjsKCSJwdXJjaGFzZS1kYXRlLXBzdCIgPSAiMjAxMi0wOS0xOSAxMzoxMDozNCBBbWVyaWNhL0xvc19BbmdlbGVzIjsKCSJvcmlnaW5hbC1wdXJjaGFzZS1kYXRlIiA9ICIyMDEyLTA5LTE5IDIwOjEwOjM0IEV0Yy9HTVQiOwp9\";" +
                "\"environment\" = \"Sandbox\";" +
                "\"pod\" = \"100\";" +
                "\"signing-status\" = \"0\";" +
                "}");
        assertEquals("Sandbox", ticket.environment);
        assertEquals("100", ticket.pod);
        assertEquals("0", ticket.signingStatus);

        Map<String, Object> purchaseInfo = ticket.purchaseInfo;
        assertEquals("2012-09-19 13:10:34 America/Los_Angeles", purchaseInfo.get("original-purchase-date-pst"));
        assertEquals("a1aba66d87b8542ccfba9ff27bd941238b2d0dd0", purchaseInfo.get("unique-identifier"));
        assertEquals("1000000056117018", purchaseInfo.get("original-transaction-id"));
        assertEquals("0.1.0", purchaseInfo.get("bvrs"));
        assertEquals("1000000056117018", purchaseInfo.get("transaction-id"));
        assertEquals("1", purchaseInfo.get("quantity"));
        assertEquals("1348085434955", purchaseInfo.get("original-purchase-date-ms"));
        assertEquals("com.glu.dragonstorm.CROWNS_PACK_F", purchaseInfo.get("product-id"));
        assertEquals("536366874", purchaseInfo.get("item-id"));
        assertEquals("com.glu.dragonstorm", purchaseInfo.get("bid"));
        assertEquals("1348085434955", purchaseInfo.get("purchase-date-ms"));
        assertEquals("2012-09-19 20:10:34 Etc/GMT", purchaseInfo.get("purchase-date"));
        assertEquals("2012-09-19 13:10:34 America/Los_Angeles", purchaseInfo.get("purchase-date-pst"));
        assertEquals("2012-09-19 20:10:34 Etc/GMT", purchaseInfo.get("original-purchase-date"));

        // and key duplicates
        assertEquals("2012-09-19 13:10:34 America/Los_Angeles", purchaseInfo.get("original_purchase_date_pst"));
        assertEquals("a1aba66d87b8542ccfba9ff27bd941238b2d0dd0", purchaseInfo.get("unique_identifier"));
        assertEquals("1000000056117018", purchaseInfo.get("original_transaction_id"));
        assertEquals("0.1.0", purchaseInfo.get("bvrs"));
        assertEquals("1000000056117018", purchaseInfo.get("transaction_id"));
        assertEquals("1", purchaseInfo.get("quantity"));
        assertEquals("1348085434955", purchaseInfo.get("original_purchase_date_ms"));
        assertEquals("com.glu.dragonstorm.CROWNS_PACK_F", purchaseInfo.get("product_id"));
        assertEquals("536366874", purchaseInfo.get("item_id"));
        assertEquals("com.glu.dragonstorm", purchaseInfo.get("bid"));
        assertEquals("1348085434955", purchaseInfo.get("purchase_date_ms"));
        assertEquals("2012-09-19 20:10:34 Etc/GMT", purchaseInfo.get("purchase_date"));
        assertEquals("2012-09-19 13:10:34 America/Los_Angeles", purchaseInfo.get("purchase_date_pst"));
        assertEquals("2012-09-19 20:10:34 Etc/GMT", purchaseInfo.get("original_purchase_date"));
    }

    @Test
    public void convertValidTicket() throws IOException {
        String rawticket = "ewoJInNpZ25hdHVyZSIgPSAiQWsxblJITm10K2gzb0E5M2RLeVFDWm1HRGFqWm5xMXBoSEtmSy9HczZOeXJoQ1hqVUpRZDBiTjB2RUpoY25ua2ZQWGNiL09wbXFranlTTEd2N0crUWVnQTAyVU1TRFRlOXc0czM5ZjJuQjZKdE1xeTZkU1E3OWh1RTB6S3U1S2U2ZXRLZjBRUWI4cGg5RjlEUVk4VXBlWmVxYlhNOFN0VnhmRk5HRzIvUjlzSEFBQURWekNDQTFNd2dnSTdvQU1DQVFJQ0NCdXA0K1BBaG0vTE1BMEdDU3FHU0liM0RRRUJCUVVBTUg4eEN6QUpCZ05WQkFZVEFsVlRNUk13RVFZRFZRUUtEQXBCY0hCc1pTQkpibU11TVNZd0pBWURWUVFMREIxQmNIQnNaU0JEWlhKMGFXWnBZMkYwYVc5dUlFRjFkR2h2Y21sMGVURXpNREVHQTFVRUF3d3FRWEJ3YkdVZ2FWUjFibVZ6SUZOMGIzSmxJRU5sY25ScFptbGpZWFJwYjI0Z1FYVjBhRzl5YVhSNU1CNFhEVEUwTURZd056QXdNREl5TVZvWERURTJNRFV4T0RFNE16RXpNRm93WkRFak1DRUdBMVVFQXd3YVVIVnlZMmhoYzJWU1pXTmxhWEIwUTJWeWRHbG1hV05oZEdVeEd6QVpCZ05WQkFzTUVrRndjR3hsSUdsVWRXNWxjeUJUZEc5eVpURVRNQkVHQTFVRUNnd0tRWEJ3YkdVZ1NXNWpMakVMTUFrR0ExVUVCaE1DVlZNd2daOHdEUVlKS29aSWh2Y05BUUVCQlFBRGdZMEFNSUdKQW9HQkFNbVRFdUxnamltTHdSSnh5MW9FZjBlc1VORFZFSWU2d0Rzbm5hbDE0aE5CdDF2MTk1WDZuOTNZTzdnaTNvclBTdXg5RDU1NFNrTXArU2F5Zzg0bFRjMzYyVXRtWUxwV25iMzRucXlHeDlLQlZUeTVPR1Y0bGpFMU93QytvVG5STStRTFJDbWVOeE1iUFpoUzQ3VCtlWnRERWhWQjl1c2szK0pNMkNvZ2Z3bzdBZ01CQUFHamNqQndNQjBHQTFVZERnUVdCQlNKYUVlTnVxOURmNlpmTjY4RmUrSTJ1MjJzc0RBTUJnTlZIUk1CQWY4RUFqQUFNQjhHQTFVZEl3UVlNQmFBRkRZZDZPS2RndElCR0xVeWF3N1hRd3VSV0VNNk1BNEdBMVVkRHdFQi93UUVBd0lIZ0RBUUJnb3Foa2lHOTJOa0JnVUJCQUlGQURBTkJna3Foa2lHOXcwQkFRVUZBQU9DQVFFQWVhSlYyVTUxcnhmY3FBQWU1QzIvZkVXOEtVbDRpTzRsTXV0YTdONlh6UDFwWkl6MU5ra0N0SUl3ZXlOajVVUllISytIalJLU1U5UkxndU5sMG5rZnhxT2JpTWNrd1J1ZEtTcTY5Tkluclp5Q0Q2NlI0Szc3bmI5bE1UQUJTU1lsc0t0OG9OdGxoZ1IvMWtqU1NSUWNIa3RzRGNTaVFHS01ka1NscDRBeVhmN3ZuSFBCZTR5Q3dZVjJQcFNOMDRrYm9pSjNwQmx4c0d3Vi9abEwyNk0ydWVZSEtZQ3VYaGRxRnd4VmdtNTJoM29lSk9PdC92WTRFY1FxN2VxSG02bTAzWjliN1BSellNMktHWEhEbU9Nazd2RHBlTVZsTERQU0dZejErVTNzRHhKemViU3BiYUptVDdpbXpVS2ZnZ0VZN3h4ZjRjemZIMHlqNXdOelNHVE92UT09IjsKCSJwdXJjaGFzZS1pbmZvIiA9ICJld29KSW05eWFXZHBibUZzTFhCMWNtTm9ZWE5sTFdSaGRHVXRjSE4wSWlBOUlDSXlNREUxTFRBMExUSXdJREUwT2pFeE9qQTVJRUZ0WlhKcFkyRXZURzl6WDBGdVoyVnNaWE1pT3dvSkluVnVhWEYxWlMxcFpHVnVkR2xtYVdWeUlpQTlJQ0kzTUdZeVpXVmxabVJoWlRFNU1qSmtaRGhpWVdJNFptUmtaR0ZoT0dNelkySTVZakkwTkdZNElqc0tDU0p2Y21sbmFXNWhiQzEwY21GdWMyRmpkR2x2YmkxcFpDSWdQU0FpTVRBd01EQXdNREUxTWpRd056WXlOeUk3Q2draVluWnljeUlnUFNBaU1DNHdMakFpT3dvSkluUnlZVzV6WVdOMGFXOXVMV2xrSWlBOUlDSXhNREF3TURBd01UVXlOREEzTmpJM0lqc0tDU0p4ZFdGdWRHbDBlU0lnUFNBaU1TSTdDZ2tpYjNKcFoybHVZV3d0Y0hWeVkyaGhjMlV0WkdGMFpTMXRjeUlnUFNBaU1UUXlPVFUyTkRJMk9UUTVOQ0k3Q2draWRXNXBjWFZsTFhabGJtUnZjaTFwWkdWdWRHbG1hV1Z5SWlBOUlDSkdNak0yT0VNd055MUdOemt6TFRRNU1qSXRPVEV4T0MwMVJrTXdSamhDUlRWQk5UTWlPd29KSW5CeWIyUjFZM1F0YVdRaUlEMGdJbU52YlM1bmJIVXVaR1ZsY21oMWJuUXhOaTVvWXpJaU93b0pJbWwwWlcwdGFXUWlJRDBnSWprNE5qazRNVGN3TnlJN0Nna2lZbWxrSWlBOUlDSmpiMjB1WjJ4MUxtUmxaWEpvZFc1ME1UWWlPd29KSW5CMWNtTm9ZWE5sTFdSaGRHVXRiWE1pSUQwZ0lqRTBNamsxTmpReU5qazBPVFFpT3dvSkluQjFjbU5vWVhObExXUmhkR1VpSUQwZ0lqSXdNVFV0TURRdE1qQWdNakU2TVRFNk1Ea2dSWFJqTDBkTlZDSTdDZ2tpY0hWeVkyaGhjMlV0WkdGMFpTMXdjM1FpSUQwZ0lqSXdNVFV0TURRdE1qQWdNVFE2TVRFNk1Ea2dRVzFsY21sallTOU1iM05mUVc1blpXeGxjeUk3Q2draWIzSnBaMmx1WVd3dGNIVnlZMmhoYzJVdFpHRjBaU0lnUFNBaU1qQXhOUzB3TkMweU1DQXlNVG94TVRvd09TQkZkR012UjAxVUlqc0tmUT09IjsKCSJlbnZpcm9ubWVudCIgPSAiU2FuZGJveCI7CgkicG9kIiA9ICIxMDAiOwoJInNpZ25pbmctc3RhdHVzIiA9ICIwIjsKfQ==";

        IapTicket ticket = converter.readTicket(converter.decode(rawticket));
        logger.info("{}", ticket);

    }
}
