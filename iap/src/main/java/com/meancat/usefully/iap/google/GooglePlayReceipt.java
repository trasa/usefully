package com.meancat.usefully.iap.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GooglePlayReceipt {

    private String orderId;
    private String productId;
    private String packageName;
    private long purchaseTime;
    private int purchaseState;
    private String developerPayload;
    private String purchaseToken;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(long purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public int getPurchaseState() {
        return purchaseState;
    }

    public void setPurchaseState(int purchaseState) {
        this.purchaseState = purchaseState;
    }

    public String getDeveloperPayload() {
        return developerPayload;
    }

    public void setDeveloperPayload(String developerPayload) {
        this.developerPayload = developerPayload;
    }

    public String getPurchaseToken() {
        return purchaseToken;
    }

    public void setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
    }

    @Override
    public String toString() {
        return "GooglePlayReceipt{" +
                "orderId='" + orderId + '\'' +
                ", productId='" + productId + '\'' +
                ", packageName='" + packageName + '\'' +
                ", purchaseTime=" + purchaseTime +
                ", purchaseState=" + purchaseState +
                ", developerPayload='" + developerPayload + '\'' +
                ", purchaseToken='" + purchaseToken + '\'' +
                '}';
    }
}
