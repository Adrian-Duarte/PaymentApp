package com.example.paymentapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
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

    public static Intent getStartIntent(Context context, User user) {
        Intent intent =  new Intent(context, AmountActivity.class);
        intent.putExtra(EXTRA_USER, user);
        return intent;
    }

    // Constants
    public static final String EXTRA_USER = "extra_user";

    // Attributes
    private EditText etAmount;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtras();
        initialize();
        setListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Update payment
        String amount = etAmount.getText().toString();
        String onlyNumber = amount.replaceAll("[^0-9]", "");
        Payment payment = new Payment();
        payment.setAmount(onlyNumber);
        new TinyDB(this).putPayment(payment);
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
    private void getExtras() {
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
            user = extras.getParcelable(EXTRA_USER);
    }

    private void initialize() {
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