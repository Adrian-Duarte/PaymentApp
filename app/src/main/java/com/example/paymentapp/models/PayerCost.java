package com.example.paymentapp.models;

import com.google.gson.annotations.SerializedName;

public class PayerCost {

    // Attributes
    @SerializedName("recommended_message")
    private String recommendedMessage;

    // Getters
    public String getRecommendedMessage() {
        return recommendedMessage;
    }

}