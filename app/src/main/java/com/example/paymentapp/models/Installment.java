package com.example.paymentapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Installment {

    // Attributes
    @SerializedName("payer_costs")
    private List<PayerCost> payerCosts;

    // Getters
    public List<PayerCost> getPayerCosts() {
        return payerCosts;
    }

}