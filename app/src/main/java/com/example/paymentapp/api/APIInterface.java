package com.example.paymentapp.api;

import com.example.paymentapp.models.data.PaymentMethod;
import com.example.paymentapp.models.data.UserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("api/users")
    Call<UserData> getUsers(@Query("per_page") int perPage);

    @GET("v1//payment_methods")
    Call<List<PaymentMethod>> getPaymentMethods(@Query("public_key") String publicKey);

}