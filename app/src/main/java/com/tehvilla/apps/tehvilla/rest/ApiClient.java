package com.tehvilla.apps.tehvilla.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by NIT NB1 on 16/01/2017.
 */

public class ApiClient {
//    public static final String BASE_URL = "http://www.arkodia.id/tehvilla/index.php/api/";
//public static final String BASE_URL = "http://manfaattehhitam.com/hsn/app/index.php/api/";
    public static final String BASE_URL = "http://manfaattehhitam.com/hsn/testing/index.php/api/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpclient = new OkHttpClient.Builder();

        httpclient.addInterceptor(logging)
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES);

        if (retrofit==null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpclient.build())
                    .build();
        }
        return retrofit;
    }
}
