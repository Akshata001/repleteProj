package com.optionsmoneymaker.optionsmoneymaker.rest;

import com.google.gson.Gson;
import com.optionsmoneymaker.optionsmoneymaker.utils.Constants;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by Sagar on 04-10-2016.
 */
public class RestClient {

    private static final long HTTP_TIMEOUT = TimeUnit.SECONDS.toMillis(300);
    private static MoneyMaker REST_CLIENT_MONEY_MAKER;
    private static String URL = Constants.API_URL;

    static {
        setupRestClient();
    }

    private RestClient() {
    }

    private static void setupRestClient() {
        REST_CLIENT_MONEY_MAKER = buildAdapter(URL).create(MoneyMaker.class);
    }

    private static RestAdapter buildAdapter(String endPoint){

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
        okHttpClient.setWriteTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);

        return new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(endPoint)
                .setClient(new OkClient(okHttpClient))
                .setConverter(new GsonConverter(new Gson()))
                .build();
    }

    public static MoneyMaker getMoneyMaker() {
        return REST_CLIENT_MONEY_MAKER;
    }
}
