package com.ariefzuhri.amigo19.rest;

import com.ariefzuhri.amigo19.response.AttributesGlobal;
import com.ariefzuhri.amigo19.response.AttributesProvince;
import com.ariefzuhri.amigo19.response.Country;
import com.ariefzuhri.amigo19.response.GlobalSummary;
import com.ariefzuhri.amigo19.response.WCUCountries;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    /* Daftar semua provinsi Indonesia */
    @GET("/indonesia/provinsi")
    Call<ArrayList<AttributesProvince>> getIndonesiaProvinces();

    /* Hanya Indonesia */
    @GET("/indonesia")
    Call<ArrayList<Country>> getIndonesia();

    /* Daftar semua negara */
    @GET("/")
    Call<ArrayList<AttributesGlobal>> getGlobal();

    /* Global summary */
    @GET("/positif")
    Call<GlobalSummary> getGlobalInfected();

    @GET("/sembuh")
    Call<GlobalSummary> getGlobalRecovered();

    @GET("/meninggal")
    Call<GlobalSummary> getGlobalDeath();

    // Only for WORLD_COUNTRIES_URL (WCU)
    @GET("/api/countries")
    Call<WCUCountries> getWorldCountries();
}
