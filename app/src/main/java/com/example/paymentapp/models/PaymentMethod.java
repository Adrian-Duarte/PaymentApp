package com.example.paymentapp.models;

import com.google.gson.annotations.SerializedName;

public class PaymentMethod {

    // Attributes
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("payment_type_id")
    private String paymentTypeId;
    @SerializedName("secure_thumbnail")
    private String secureThumbnail;

    // Getters
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPaymentTypeId() {
        return paymentTypeId;
    }
    public String getSecureThumbnail() {
        return secureThumbnail;
    }

}