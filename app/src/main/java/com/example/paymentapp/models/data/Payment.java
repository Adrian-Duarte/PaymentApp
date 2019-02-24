package com.example.paymentapp.models.data;

public class Payment {

    // Attributes
    private String id;
    private String amount;
    private String recommendedMessage;
    private String issuerId;

    // Getters && Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAmount() {
        return amount;
    }
    public void setRecommendedMessage(String recommendedMessage) {
        this.recommendedMessage = recommendedMessage;
    }
    public String getRecommendedMessage() {
        return recommendedMessage;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getIssuerId() {
        return issuerId;
    }
    public void setIssuerId(String issuerId) {
        this.issuerId = issuerId;
    }

}