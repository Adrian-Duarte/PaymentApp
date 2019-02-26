package com.example.paymentapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paymentapp.R;

public abstract class BaseActivity extends AppCompatActivity {

    public abstract boolean addBackButton();
    public abstract int getContentView();
    public abstract int getToolbarTitle();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set support action bar
        setContentView(getContentView());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set title
        ((TextView)toolbar.findViewById(R.id.tv_toolbar_title)).setText(getString(getToolbarTitle()));

        // Show back button
        ActionBar actionBar = getSupportActionBar();
        if(addBackButton() && actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Protected methods
    protected void showGenericError() {
        Toast.makeText(this, getString(R.string.all_generic_error), Toast.LENGTH_SHORT).show();
    }

}