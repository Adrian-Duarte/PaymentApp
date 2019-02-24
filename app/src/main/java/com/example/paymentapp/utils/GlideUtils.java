package com.example.paymentapp.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;

public class GlideUtils {

    public static void load(Context context, String image, ImageView imageView) {
        RequestManager requestManager = Glide.with(context);
        RequestBuilder requestBuilder = requestManager.load(image);
        requestBuilder.into(imageView);
    }

}