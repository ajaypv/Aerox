//package com.example.aexox;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.Activity;
//import android.os.Bundle;
//
//import com.google.android.gms.wallet.PaymentsClient;
//import com.google.android.gms.wallet.Wallet;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//
//public class Payment extends AppCompatActivity {
//
//    private static JSONObject getBaseRequest() throws JSONException {
//        return new JSONObject().put("apiVersion", 2).put("apiVersionMinor", 0);
//    }
//    public static PaymentsClient createPaymentsClient(Activity activity) {
//        Wallet.WalletOptions walletOptions =
//                new Wallet.WalletOptions.Builder().setEnvironment(Constants.PAYMENTS_ENVIRONMENT).build();
//        return Wallet.getPaymentsClient(activity, walletOptions);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_payment);
//
//    }
//}