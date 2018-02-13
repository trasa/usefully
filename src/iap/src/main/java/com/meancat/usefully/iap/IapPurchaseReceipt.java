package com.meancat.usefully.iap;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

public class IapPurchaseReceipt {
    // fields from the ticket
    private String signature;
    private String pod;
    private String signingStatus;
    private String environment;

    // fields from the purchaseinfo inside the ticket
    private String bid;
    private String transactionId;
    private String quantity;
    private String productId;

    private Map<String, Object> purchaseInfo = newHashMap();

    // values that don't come from IapTicket at all
    private boolean isValid;
    private boolean isConfirmed;
    private String playerId;

    public IapPurchaseReceipt() {
    }

    public IapPurchaseReceipt(String playerId) {
        this.playerId = playerId;
    }


    public static IapPurchaseReceipt createFrom(IapTicket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("ticket can't be null");
        }
        IapPurchaseReceipt receipt = new IapPurchaseReceipt();
        receipt.signature = ticket.signature;
        receipt.pod = ticket.pod;
        receipt.signingStatus = ticket.signingStatus;
        receipt.environment = ticket.environment;
        receipt.purchaseInfo = ticket.purchaseInfo;

        receipt.bid = ticket.getPurchaseInfoField(IapTicket.PurchaseInfo.BID);
        receipt.transactionId = ticket.getPurchaseInfoField(IapTicket.PurchaseInfo.TRANSACTION_ID);
        receipt.quantity = ticket.getPurchaseInfoField(IapTicket.PurchaseInfo.QUANTITY);
        receipt.productId = ticket.getPurchaseInfoField(IapTicket.PurchaseInfo.PRODUCT_ID);

        receipt.isConfirmed = false;
        receipt.isValid = false;
        return receipt;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getSigningStatus() {
        return signingStatus;
    }

    public void setSigningStatus(String signingStatus) {
        this.signingStatus = signingStatus;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Map<String, Object> getPurchaseInfo() {
        return purchaseInfo;
    }

    public void setPurchaseInfo(Map<String, Object> purchaseInfo) {
        this.purchaseInfo = purchaseInfo;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
