package com.scsvn.whc_2016.retrofit;

import android.content.Context;

import com.scsvn.whc_2016.preferences.SettingPref;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Trần Xuân Lộc on 1/3/2016.
 */
public class MyRetrofit {
    //    public static final String BASE_URL = "http://115.78.13.10:810";
    public static MyRequests initRequest(Context context) {
        String BASE_URL = String.format("http://%s", SettingPref.getInfoNetwork(context)[0]);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(0, TimeUnit.SECONDS);
        client.setWriteTimeout(0, TimeUnit.SECONDS);
        client.setReadTimeout(0, TimeUnit.SECONDS);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(MyRequests.class);
    }
}
