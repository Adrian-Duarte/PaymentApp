package com.example.paymentapp.models;

import com.google.gson.annotations.SerializedName;

public class Bank {

    // Attributes
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("secure_thumbnail")
    private String secureThumbnail;

    // Getters
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getSecureThumbnail() {
        return secureThumbnail;
    }

}