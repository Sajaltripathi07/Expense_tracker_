package com.example.expensetracker.utils;

import java.text.DecimalFormat;

public class NumberFormatter {

    public static String formatDecimal(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(amount);
    }
}
