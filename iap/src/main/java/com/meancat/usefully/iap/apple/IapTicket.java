
package com.meancat.usefully.iap.apple;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IapTicket {

    public String signature;
    @JsonProperty(value = "purchase-info")
    public String rawPurchaseInfo;
    public String pod;
    @JsonProperty(value="signing-status")
    public String signingStatus;
    public String environment;

    // this is populated by IapTicketConverter from the decoded and json-deserialized rawPurchaseInfo.
    @JsonIgnore
    public Map<String, Object> purchaseInfo = newHashMap();

    public String getPurchaseInfoField(PurchaseInfo field) {
        Object o = purchaseInfo.get(field.getKey());
        if (o != null)
            return o.toString();
        return null;
    }

    public void setPurchaseInfoField(PurchaseInfo field, String value) {
        purchaseInfo.put(field.getKey(), value);
    }

    @Override
    public String toString() {
        return "IapTicket{" +
                "signature=" + (signature == null ? "(null)" : "(not null)")  +
                ", pod='" + pod + '\'' +
                ", signingStatus='" + signingStatus + '\'' +
                ", environment='" + environment + '\'' +
                ", purchaseInfo=" + purchaseInfo +
                '}';
    }

    /**
     * Known keys in the purchaseInfo map.
     */
    public static enum PurchaseInfo {
        ORIGINAL_PURCHASE_DATE_PST("original_purchase_date_pst"),
        UNIQUE_IDENTIFIER("unique_identifier"),
        ORIGINAL_TRANSACTION_ID("original_transaction_id"),
        BVRS("bvrs"),
        TRANSACTION_ID("transaction_id"),
        QUANTITY("quantity"),
        ORIGINAL_PURCHASE_DATE_MS("original_purchase_date_ms"),
        PRODUCT_ID("product_id"),
        ITEM_ID("item_id"),
        BID("bid"),
        PURCHASE_DATE_MS("purchase_date_ms"),
        PURCHASE_DATE("purchase_date"),
        PURCHASE_DATE_PST("purchase_date_pst"),
        ORIGINAL_PURCHASE_DATE("original_purchase_date");


        private String key;
        PurchaseInfo(String key) {
            this.key = key;
        }

        public String getKey() { return key; }

    }
}
