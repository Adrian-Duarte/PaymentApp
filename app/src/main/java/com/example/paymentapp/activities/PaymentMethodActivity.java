package com.example.paymentapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.paymentapp.R;
import com.example.paymentapp.adapters.PaymentMethodRecyclerViewAdapter;
import com.example.paymentapp.api.APIClient;
import com.example.paymentapp.api.APIInterface;
import com.example.paymentapp.models.data.Payment;
import com.example.paymentapp.models.data.PaymentMethod;
import com.example.paymentapp.utils.CustomProgressBar;
import com.example.paymentapp.utils.TinyDB;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentMethodActivity extends BaseActivity implements PaymentMethodRecyclerViewAdapter.OnItemClickListener {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, PaymentMethodActivity.class);
    }

    // Attributes
    private APIInterface apiInterface;
    private CustomProgressBar customProgressBar;
    private List<PaymentMethod> paymentMethods;
    private RecyclerView rvPaymentMethods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
        getPaymentMethods();
    }

    @Override
    public boolean addBackButton() {
        return true;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_payment_method;
    }

    @Override
    public void onItemClick(PaymentMethod paymentMethod) {
        // Update payment
        TinyDB tinyDB = new TinyDB(this);
        Payment payment = tinyDB.getPayment();
        payment.setId(paymentMethod.getId());
        tinyDB.putPayment(payment);

        // Start bank activity
        startActivity(BankActivity.getStartIntent(this, paymentMethod.getId()));
    }

    // Private methods
    private void getPaymentMethods() {
        customProgressBar.show();
        Call<List<PaymentMethod>> call = apiInterface.getPaymentMethods(APIClient.PUBLIC_KEY);
        call.enqueue(new Callback<List<PaymentMethod>>() {
            @Override
            public void onResponse(Call<List<PaymentMethod>> call, Response<List<PaymentMethod>> response) {
                customProgressBar.hide();

                // Check if data is correct
                if(!response.isSuccessful()) {
                    return;
                }
                paymentMethods = response.body();
                if(paymentMethods ==null) {
                    return;
                }

                // Success
                updatePaymentMethods();
            }
            @Override
            public void onFailure(Call<List<PaymentMethod>> call, Throwable t) {
                call.cancel();
                customProgressBar.hide();
            }
        });
    }

    private void initialize() {
        apiInterface = APIClient.getMercadoPagoClient().create(APIInterface.class);
        customProgressBar = new CustomProgressBar(this);
        rvPaymentMethods = findViewById(R.id.rv_payment_methods);
        rvPaymentMethods.setLayoutManager(new LinearLayoutManager(this));
        rvPaymentMethods.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvPaymentMethods.setItemAnimator(new DefaultItemAnimator());
    }

    private void updatePaymentMethods() {
        PaymentMethodRecyclerViewAdapter paymentMethodRecyclerViewAdapter = new PaymentMethodRecyclerViewAdapter(this, paymentMethods);
        rvPaymentMethods.setAdapter(paymentMethodRecyclerViewAdapter);
    }

}