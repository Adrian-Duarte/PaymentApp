package com.example.paymentapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paymentapp.R;
import com.example.paymentapp.models.User;
import com.example.paymentapp.models.data.Payment;
import com.example.paymentapp.utils.CurrencyUtils;
import com.example.paymentapp.utils.GlideUtils;
import com.example.paymentapp.utils.TinyDB;

public class AmountActivity extends BaseActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AmountActivity.class);
    }

    // Attributes
    private EditText etAmount;
    private User user;

    // Override methods and callbacks
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
        setListeners();
    }

    @Override
    public boolean addBackButton() {
        return true;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_amount;
    }

    // Private methods
    private void initialize() {
        user = new TinyDB(this).getPayment().getUser();
        etAmount = findViewById(R.id.et_amount);
        etAmount.requestFocus();
        etAmount.setHint(CurrencyUtils.formatCurrency(0));
        GlideUtils.load(this, user.getAvatar(), (ImageView) findViewById(R.id.iv_avatar));
        ((TextView) findViewById(R.id.tv_full_name)).setText(user.getFullName());
    }

    private void setListeners() {
        etAmount.addTextChangedListener(textWatcher);
    }

    // Listeners
    public void onClickContinueButton(View view) {
        // Update payment
        TinyDB tinyDB = new TinyDB(this);
        String amount = etAmount.getText().toString();
        String onlyNumber = amount.replaceAll("[^0-9]", "");
        Payment payment = tinyDB.getPayment();
        payment.setAmount(onlyNumber);
        new TinyDB(this).putPayment(payment);

        // Start payment method activity
        startActivity(PaymentMethodActivity.getStartIntent(this));
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!s.toString().isEmpty()) {
                etAmount.removeTextChangedListener(this);
                String formatted = CurrencyUtils.formatCurrency(s);
                etAmount.setText(formatted);
                etAmount.setSelection(formatted.length());
                etAmount.addTextChangedListener(this);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

}