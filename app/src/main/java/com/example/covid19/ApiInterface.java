package com.example.covid19;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    final String BASE_URL = "https://corona.lmao.ninja/v2/";

    @GET("countries")
    Call<List<ModelClass>> getCountryData();

}
