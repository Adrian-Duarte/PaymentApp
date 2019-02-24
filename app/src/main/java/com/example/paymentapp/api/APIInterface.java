package com.example.paymentapp.api;

import com.example.paymentapp.models.data.UserData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("api/users")
    Call<UserData> getUsers(@Query("per_page") int perPage);

}