package com.example.paymentapp.models.data;

import com.example.paymentapp.models.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserData extends BaseModelData {

    // Attributes
    @SerializedName("data")
    private List<User> users;

    // Getters
    public List<User> getUsers() {
        return users;
    }

}
