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
import com.example.paymentapp.adapters.PaymentMethodRecyclerViewAdapter;
import com.example.paymentapp.api.APIClient;
import com.example.paymentapp.api.APIInterface;
import com.example.paymentapp.models.Payment;
import com.example.paymentapp.models.PaymentMethod;
import com.example.paymentapp.utils.CustomProgressBar;
import com.example.paymentapp.utils.TinyDB;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentMethodActivity extends BaseActivity implements PaymentMethodRecyclerViewAdapter.OnItemClickListener {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, PaymentMethodActivity.class);
    }

    // Constants
    private static final String DESIRED_PAYMENT_TYPE_ID = "credit_card";

    // Attributes
    private APIInterface apiInterface;
    private CustomProgressBar customProgressBar;
    private List<PaymentMethod> paymentMethods;
    private PaymentMethodRecyclerViewAdapter paymentMethodRecyclerViewAdapter;
    private RecyclerView rvPaymentMethods;
    private SwipeRefreshLayout srlPaymentMethods;

    // Override methods and callbacks
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
        setListeners();
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
    public int getToolbarTitle() {
        return R.string.all_payment_method;
    }

    @Override
    public void onItemClick(PaymentMethod paymentMethod) {
        // Update payment
        TinyDB tinyDB = new TinyDB(this);
        Payment payment = tinyDB.getPayment();
        payment.setPaymentMethod(paymentMethod);
        tinyDB.putPayment(payment);

        // Start bank activity
        startActivity(BankActivity.getStartIntent(this, paymentMethod.getId()));
    }

    // Private methods
    private void getPaymentMethods() {
        Call<List<PaymentMethod>> call = apiInterface.getPaymentMethods(APIClient.PUBLIC_KEY);
        call.enqueue(new Callback<List<PaymentMethod>>() {
            @Override
            public void onResponse(Call<List<PaymentMethod>> call, Response<List<PaymentMethod>> response) {
                customProgressBar.hide();
                srlPaymentMethods.setRefreshing(false);

                // Check if data is correct
                if(!response.isSuccessful()) {
                    showGenericError();
                    return;
                }
                paymentMethods = response.body();
                if(paymentMethods==null) {
                    showGenericError();
                    return;
                }

                // Get only credit card types
                Iterator<PaymentMethod> iterator = paymentMethods.iterator();
                while (iterator.hasNext()) {
                    PaymentMethod paymentMethod = iterator.next();
                    if(!paymentMethod.getPaymentTypeId().equals(DESIRED_PAYMENT_TYPE_ID))
                        iterator.remove();
                }

                // Success
                updatePaymentMethods();
            }
            @Override
            public void onFailure(Call<List<PaymentMethod>> call, Throwable t) {
                call.cancel();
                customProgressBar.hide();
                srlPaymentMethods.setRefreshing(false);
                showGenericError();
            }
        });
    }

    private void initialize() {
        apiInterface = APIClient.getMercadoPagoClient().create(APIInterface.class);
        customProgressBar = new CustomProgressBar(this);
        customProgressBar.show();
        rvPaymentMethods = findViewById(R.id.rv_payment_methods);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (paymentMethodRecyclerViewAdapter.getItemViewType(position)) {
                    case PaymentMethodRecyclerViewAdapter.BODY_VIEW:
                        return 1;
                    default:
                        return 3;
                }
            }
        });
        rvPaymentMethods.setLayoutManager(gridLayoutManager);
        rvPaymentMethods.setItemAnimator(new DefaultItemAnimator());
        srlPaymentMethods = findViewById(R.id.srl_payment_methods);
    }

    private void setListeners() {
        srlPaymentMethods.setOnRefreshListener(onRefreshListener);
    }

    private void updatePaymentMethods() {
        paymentMethodRecyclerViewAdapter = new PaymentMethodRecyclerViewAdapter(this, paymentMethods);
        rvPaymentMethods.setAdapter(paymentMethodRecyclerViewAdapter);
    }

    // Private methods
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getPaymentMethods();
        }
    };

}