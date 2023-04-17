package com.aeroxlive.aeroxapplication;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PaymentSharedPreferences {
    private static final String PREFS_NAME = "PaymentPrefs";
    private static final String KEY_PAYMENT_LIST = "paymentList";

    private SharedPreferences mPrefs;

    public PaymentSharedPreferences(Context context) {
        mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void savePaymentList(List<PaymentModel> paymentList) {
        Gson gson = new Gson();
        String json = gson.toJson(paymentList);
        mPrefs.edit().putString(KEY_PAYMENT_LIST, json).apply();
    }

    public List<PaymentModel> getPaymentList() {
        Gson gson = new Gson();
        String json = mPrefs.getString(KEY_PAYMENT_LIST, "");
        Type type = new TypeToken<List<PaymentModel>>() {}.getType();
        List<PaymentModel> paymentList = gson.fromJson(json, type);
        if (paymentList == null) {
            mPrefs.edit().clear().apply();
            return new ArrayList<>();
        }
        return paymentList;
    }
}
