package com.example.paymentapp.utils;

import java.text.NumberFormat;

public class CurrencyUtils {

    public static String formatCurrency(CharSequence number) {
        return formatCurrency(number.toString());
    }

    public static String formatCurrency(String number) {
        String formattedCurrency = number.replaceAll("[^0-9]", "");
        if(!formattedCurrency.isEmpty()) {
            double parsed = Double.parseDouble(formattedCurrency);
            return formatCurrency(parsed);
        }
        return formattedCurrency;
    }

    public static String formatCurrency(double number) {
        return NumberFormat.getCurrencyInstance().format(number);
    }

}