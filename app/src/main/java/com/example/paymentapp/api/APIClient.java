package com.example.paymentapp.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    // Constants
    private static final String MERCADO_PAGO_BASE_URL = "https://api.mercadopago.com/";
    private static final String MOCK_BASE_URL = "https://reqres.in/";
    public static final String PUBLIC_KEY = "444a9ef5-8a6b-429f-abdf-587639155d88";

    // Attributes
    private static Retrofit retrofit = null;

    public static Retrofit getMercadoPagoClient() {
        return getClient(MERCADO_PAGO_BASE_URL);
    }

    public static Retrofit getMockClient() {
        return getClient(MOCK_BASE_URL);
    }

    private static Retrofit getClient(String baseUrl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        ;
        return retrofit;
    }

}