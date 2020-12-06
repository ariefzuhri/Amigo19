package com.ariefzuhri.amigo19.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.ariefzuhri.amigo19.response.Country;
import com.ariefzuhri.amigo19.rest.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ariefzuhri.amigo19.utils.AppUtils.LOG;

public class IndonesiaViewModel extends ViewModel {
    private static final String TAG = "IndonesiaViewModel";

    private MutableLiveData<ArrayList<Country>> indonesia = new MutableLiveData<>();

    public LiveData<ArrayList<Country>> getIndonesia() {
        return indonesia;
    }

    public void setIndonesia(){
        RetrofitClient client = new RetrofitClient();
        client.getService().getIndonesia().enqueue(new Callback<ArrayList<Country>>() {
            @Override
            public void onResponse(@NotNull Call<ArrayList<Country>> call, @NotNull Response<ArrayList<Country>> response) {
                try{
                    if (response.isSuccessful()) indonesia.postValue(response.body());
                } catch (Exception e){
                    Log.d(TAG, LOG + "exception:" + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<ArrayList<Country>> call, @NotNull Throwable t) {
                Log.d(TAG, LOG + "failure:" + t.getMessage());
            }
        });
    }
}
