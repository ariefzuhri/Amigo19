package com.ariefzuhri.amigo19.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.ariefzuhri.amigo19.response.WCUCountries;
import com.ariefzuhri.amigo19.rest.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.ariefzuhri.amigo19.rest.RetrofitClient.WORLD_COUNTRIES_URL;
import static com.ariefzuhri.amigo19.utils.AppUtils.LOG;

public class WorldCountriesViewModel extends ViewModel {
    private static final String TAG = "WorldCountriesViewModel";

    private MutableLiveData<WCUCountries> listWorldCountries = new MutableLiveData<>();

    public LiveData<WCUCountries> getWorldCountries() {
        return listWorldCountries;
    }

    public void setWorldCountries(){
        RetrofitClient client = new RetrofitClient(WORLD_COUNTRIES_URL);
        final WCUCountries[] itemFound = {new WCUCountries()};
        client.getService().getWorldCountries().enqueue(new Callback<WCUCountries>() {
            @Override
            public void onResponse(@NotNull Call<WCUCountries> call, @NotNull Response<WCUCountries> response) {
                try {
                    if (response.isSuccessful()) itemFound[0] = response.body();
                    listWorldCountries.postValue(itemFound[0]);
                }catch (Exception e){
                    Log.d(TAG, LOG + "exception:" + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<WCUCountries> call, @NotNull Throwable t) {
                Log.d(TAG, LOG + "failure:" + t.getMessage());
            }
        });
    }
}
