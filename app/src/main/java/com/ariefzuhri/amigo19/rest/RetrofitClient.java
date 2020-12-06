package com.ariefzuhri.amigo19.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String WORLD_COUNTRIES_URL = "https://covid19.mathdro.id";
    private static final String BASE_URL = "https://api.kawalcorona.com";
    private static ApiService apiService;
    private static final String NEWS_URL = "http://newsapi.org/v2/top-headlines?q=covid&country=id&apiKey=***REMOVED***";

    public RetrofitClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public RetrofitClient(String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public ApiService getService(){
        return apiService;
    }
}
