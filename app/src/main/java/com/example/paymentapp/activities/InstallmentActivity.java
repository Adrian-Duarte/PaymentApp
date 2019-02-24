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
import com.example.paymentapp.adapters.PayerCostRecyclerViewAdapter;
import com.example.paymentapp.api.APIClient;
import com.example.paymentapp.api.APIInterface;
import com.example.paymentapp.models.Installment;
import com.example.paymentapp.models.PayerCost;
import com.example.paymentapp.models.data.Payment;
import com.example.paymentapp.utils.CustomProgressBar;
import com.example.paymentapp.utils.TinyDB;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstallmentActivity extends BaseActivity implements PayerCostRecyclerViewAdapter.OnItemClickListener {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, InstallmentActivity.class);
    }

    // Attributes
    private APIInterface apiInterface;
    private CustomProgressBar customProgressBar;
    private List<PayerCost> payerCosts;
    private Payment payment;
    private RecyclerView rvInstallments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
        getInstallments();
    }

    @Override
    public boolean addBackButton() {
        return true;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_installment;
    }

    @Override
    public void onItemClick(PayerCost payerCost) {
        // Update payment
        TinyDB tinyDB = new TinyDB(this);
        Payment payment = tinyDB.getPayment();
        payment.setRecommendedMessage(payerCost.getRecommendedMessage());
        tinyDB.putPayment(payment);
    }

    // Private methods
    private void getInstallments() {
        customProgressBar.show();
        Call<List<Installment>> call =
            apiInterface.getInstallments
            (
                APIClient.PUBLIC_KEY,
                payment.getAmount(),
                payment.getId(),
                payment.getIssuerId()
            )
        ;
        call.enqueue(new Callback<List<Installment>>() {
            @Override
            public void onResponse(Call<List<Installment>> call, Response<List<Installment>> response) {
                customProgressBar.hide();

                // Check if data is correct
                if(!response.isSuccessful()) {
                    return;
                }
                List<Installment> installments = response.body();
                if(installments ==null) {
                    return;
                }
                payerCosts = installments.get(0).getPayerCosts();
                if(payerCosts==null) {
                    return;
                }

                // Success
                updateInstallments();
            }
            @Override
            public void onFailure(Call<List<Installment>> call, Throwable t) {
                call.cancel();
                customProgressBar.hide();
            }
        });
    }

    private void initialize() {
        apiInterface = APIClient.getMercadoPagoClient().create(APIInterface.class);
        customProgressBar = new CustomProgressBar(this);
        payment = new TinyDB(this).getPayment();
        rvInstallments = findViewById(R.id.rv_installments);
        rvInstallments.setLayoutManager(new LinearLayoutManager(this));
        rvInstallments.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvInstallments.setItemAnimator(new DefaultItemAnimator());
    }

    private void updateInstallments() {
        PayerCostRecyclerViewAdapter payerCostRecyclerViewAdapter = new PayerCostRecyclerViewAdapter(this, payerCosts);
        rvInstallments.setAdapter(payerCostRecyclerViewAdapter);
    }

}