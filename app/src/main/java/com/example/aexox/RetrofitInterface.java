package com.example.aexox;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @POST("otpCheck")
    Call<ProfileModal> executeLogin(@Body HashMap<String, String> map);

    @POST("SendOtp")
    Call<Void> executeSignup (@Body HashMap<String, String> map);

    @POST("printPdf")
    Call<Void> executePrint(@Body HashMap<String, String> map);
}
