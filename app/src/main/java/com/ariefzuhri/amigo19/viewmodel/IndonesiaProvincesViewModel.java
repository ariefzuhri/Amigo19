package com.ariefzuhri.amigo19.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.ariefzuhri.amigo19.response.AttributesProvince;
import com.ariefzuhri.amigo19.rest.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ariefzuhri.amigo19.utils.AppUtils.LOG;

public class IndonesiaProvincesViewModel extends ViewModel {
    private static final String TAG = "IndonesiaProvincesVM";

    private MutableLiveData<ArrayList<AttributesProvince>> listIndonesiaProvinces = new MutableLiveData<>();

    public LiveData<ArrayList<AttributesProvince>> getIndonesiaProvinces() {
        return listIndonesiaProvinces;
    }

    public void setIndonesiaProvinces(){
        RetrofitClient client = new RetrofitClient();
        final ArrayList<AttributesProvince> listFound = new ArrayList<>();
        client.getService().getIndonesiaProvinces().enqueue(new Callback<ArrayList<AttributesProvince>>() {
            @Override
            public void onResponse(@NotNull Call<ArrayList<AttributesProvince>> call, @NotNull Response<ArrayList<AttributesProvince>> response) {
                try {
                    if (response.isSuccessful()){
                        ArrayList<AttributesProvince> indonesiaProvinces = response.body();
                        listFound.addAll(indonesiaProvinces);
                    }
                    listIndonesiaProvinces.postValue(listFound);
                } catch (Exception e){
                    Log.d(TAG, LOG + "exception:" + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<ArrayList<AttributesProvince>> call, @NotNull Throwable t) {
                Log.d(TAG, LOG + "failure:" + t.getMessage());
            }
        });
    }
}
