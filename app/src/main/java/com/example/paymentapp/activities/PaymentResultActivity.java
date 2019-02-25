package com.example.paymentapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paymentapp.R;
import com.example.paymentapp.models.PayerCost;
import com.example.paymentapp.models.User;
import com.example.paymentapp.models.data.Bank;
import com.example.paymentapp.models.data.Payment;
import com.example.paymentapp.models.data.PaymentMethod;
import com.example.paymentapp.utils.CurrencyUtils;
import com.example.paymentapp.utils.GlideUtils;
import com.example.paymentapp.utils.TinyDB;

public class PaymentResultActivity extends BaseActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, PaymentResultActivity.class);
    }

    // Attributes
    private TinyDB tinyDB;

    // Override methods and callbacks
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    @Override
    public boolean addBackButton() {
        return true;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_payment_result;
    }

    // Private methods
    private void initialize() {
        tinyDB = new TinyDB(this);
        Payment payment = tinyDB.getPayment();

        // User
        User user = payment.getUser();
        GlideUtils.load(this, user.getAvatar(), (ImageView) findViewById(R.id.iv_avatar));
        ((TextView) findViewById(R.id.tv_full_name)).setText(user.getFullName());

        // Amount
        ((TextView) findViewById(R.id.tv_amount)).setText(CurrencyUtils.formatCurrency(payment.getAmount()));

        // Payment method
        PaymentMethod paymentMethod = payment.getPaymentMethod();
        GlideUtils.load(this, paymentMethod.getSecureThumbnail(), (ImageView) findViewById(R.id.iv_method_secure_thumbnail));
        ((TextView) findViewById(R.id.tv_method_name)).setText(paymentMethod.getName());

        // Bank
        Bank bank = payment.getBank();
        GlideUtils.load(this, bank.getSecureThumbnail(), (ImageView) findViewById(R.id.iv_bank_secure_thumbnail));
        ((TextView) findViewById(R.id.tv_bank_name)).setText(bank.getName());

        // Payer cost
        PayerCost payerCost = payment.getPayerCost();
        ((TextView) findViewById(R.id.tv_recommended_message)).setText(payerCost.getRecommendedMessage());
    }

    public void onClickFinishButton(View view) {
        // Clear tiny db
        tinyDB.clear();

        // Start list user activity
        Intent intent = ListUserSearchActivity.getStartIntent(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}