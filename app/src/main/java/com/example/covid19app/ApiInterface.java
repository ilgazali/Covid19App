package com.example.covid19app;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    final String BASE_URL = "https://corona.lmao.ninja/v2/";

    @GET("countries")
    Call<List<ModelClass>> getCountryData();

}
