package com.example.paymentapp.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.paymentapp.R;

public class CustomProgressBar {

    // Attributes
    private View childView;
    private Activity context;
    private boolean isLoading = false;
    private ViewGroup viewGroup;

    // Constructors
    public CustomProgressBar(Context context) {
        if(context instanceof Activity)
            initialize((Activity) context);
    }

    private void initialize(Activity context) {
        this.context = context;
        viewGroup = context.findViewById(android.R.id.content);
        childView = context.getLayoutInflater().inflate(R.layout.custom_progress_bar, null);
    }

    // Public methods
    public void hide() {
        isLoading = false;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewGroup.removeView(childView);
            }
        });
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void show() {
        isLoading = true;
        viewGroup.addView(childView);
    }

}