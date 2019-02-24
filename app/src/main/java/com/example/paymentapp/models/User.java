package com.example.paymentapp.models;

import com.google.gson.annotations.SerializedName;

public class User {

    // Attributes
    @SerializedName("id")
    private Integer id;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;

    // Getters
    public Integer getId() {
        return id;
    }
    public String getAvatar() {
        return avatar;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getFullName() {
        return firstName + " " + lastName;
    }
    public String getLastName() {
        return lastName;
    }

}