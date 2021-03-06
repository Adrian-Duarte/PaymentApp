package com.example.paymentapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.paymentapp.R;
import com.example.paymentapp.adapters.BankRecyclerViewAdapter;
import com.example.paymentapp.api.APIClient;
import com.example.paymentapp.api.APIInterface;
import com.example.paymentapp.models.Bank;
import com.example.paymentapp.models.Payment;
import com.example.paymentapp.utils.CustomProgressBar;
import com.example.paymentapp.utils.TinyDB;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankActivity extends BaseActivity implements BankRecyclerViewAdapter.OnItemClickListener {

    public static Intent getStartIntent(Context context, String paymentMethodId) {
        Intent intent =  new Intent(context, BankActivity.class);
        intent.putExtra(EXTRA_PAYMENT_METHOD_ID, paymentMethodId);
        return intent;
    }

    // Constants
    public static final String EXTRA_PAYMENT_METHOD_ID = "payment_method_id";

    // Attributes
    private APIInterface apiInterface;
    private BankRecyclerViewAdapter bankRecyclerViewAdapter;
    private CustomProgressBar customProgressBar;
    private List<Bank> banks;
    private String paymentMethodId;
    private RecyclerView rvBanks;
    private SwipeRefreshLayout srlBanks;

    // Override methods and callbacks
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtras();
        initialize();
        setListeners();
        getBanks();
    }

    @Override
    public boolean addBackButton() {
        return true;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_bank;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.all_bank;
    }

    @Override
    public void onItemClick(Bank bank) {
        // Update payment
        TinyDB tinyDB = new TinyDB(this);
        Payment payment = tinyDB.getPayment();
        payment.setBank(bank);
        tinyDB.putPayment(payment);

        // Start instalment activity
        startActivity(InstallmentActivity.getStartIntent(this));
    }

    // Private methods
    private void getBanks() {
        Call<List<Bank>> call = apiInterface.getBanks(APIClient.PUBLIC_KEY, paymentMethodId);
        call.enqueue(new Callback<List<Bank>>() {
            @Override
            public void onResponse(Call<List<Bank>> call, Response<List<Bank>> response) {
                customProgressBar.hide();
                srlBanks.setRefreshing(false);

                // Check if data is correct
                if(!response.isSuccessful()) {
                    showGenericError();
                    return;
                }
                banks = response.body();
                if(banks==null) {
                    showGenericError();
                    return;
                }

                // Success
                updateBanks();
            }
            @Override
            public void onFailure(Call<List<Bank>> call, Throwable t) {
                call.cancel();
                customProgressBar.hide();
                srlBanks.setRefreshing(false);
                showGenericError();
            }
        });
    }

    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
            paymentMethodId = extras.getString(EXTRA_PAYMENT_METHOD_ID);
    }

    private void initialize() {
        apiInterface = APIClient.getMercadoPagoClient().create(APIInterface.class);
        customProgressBar = new CustomProgressBar(this);
        customProgressBar.show();
        rvBanks = findViewById(R.id.rv_banks);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (bankRecyclerViewAdapter.getItemViewType(position)) {
                    case BankRecyclerViewAdapter.BODY_VIEW:
                        return 1;
                    default:
                        return 2;
                }
            }
        });
        rvBanks.setLayoutManager(gridLayoutManager);
        rvBanks.setItemAnimator(new DefaultItemAnimator());
        srlBanks = findViewById(R.id.srl_banks);
    }

    private void setListeners() {
        srlBanks.setOnRefreshListener(onRefreshListener);
    }

    private void updateBanks() {
        bankRecyclerViewAdapter = new BankRecyclerViewAdapter(this, banks);
        rvBanks.setAdapter(bankRecyclerViewAdapter);
    }

    // Private methods
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getBanks();
        }
    };

}