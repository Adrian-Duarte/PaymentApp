package com.example.paymentapp.api;

import com.example.paymentapp.models.Installment;
import com.example.paymentapp.models.Bank;
import com.example.paymentapp.models.PaymentMethod;
import com.example.paymentapp.models.data.UserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("v1/payment_methods/card_issuers")
    Call<List<Bank>> getBanks(@Query("public_key") String publicKey, @Query("payment_method_id") String paymentMethodId);

    @GET("v1/payment_methods/installments")
    Call<List<Installment>> getInstallments(
        @Query("public_key") String publicKey,
        @Query("amount") String amount,
        @Query("payment_method_id") String paymentMethodId,
        @Query("issuer.id") String issuerId
    );

    @GET("v1/payment_methods")
    Call<List<PaymentMethod>> getPaymentMethods(@Query("public_key") String publicKey);

    @GET("api/users")
    Call<UserData> getUsers(@Query("per_page") int perPage);

}