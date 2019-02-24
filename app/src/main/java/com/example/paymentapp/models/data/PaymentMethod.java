package com.example.paymentapp.models.data;

import com.google.gson.annotations.SerializedName;

public class PaymentMethod {

    // Attributes
    @SerializedName("name")
    private String name;
    @SerializedName("secure_thumbnail")
    private String secureThumbnail;

    // Getters
    public String getName() {
        return name;
    }
    public String getSecureThumbnail() {
        return secureThumbnail;
    }

}