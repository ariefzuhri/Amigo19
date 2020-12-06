package com.ariefzuhri.amigo19.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.ariefzuhri.amigo19.response.AttributesGlobal;
import com.ariefzuhri.amigo19.rest.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ariefzuhri.amigo19.utils.AppUtils.LOG;

public class GlobalViewModel extends ViewModel {
    private static final String TAG = "GlobalViewModel";

    private MutableLiveData<ArrayList<AttributesGlobal>> listGlobal = new MutableLiveData<>();

    public LiveData<ArrayList<AttributesGlobal>> getGlobal() {
        return listGlobal;
    }

    public void setGlobal() {
        RetrofitClient client = new RetrofitClient();
        final ArrayList<AttributesGlobal> listFound = new ArrayList<>();
        client.getService().getGlobal().enqueue(new Callback<ArrayList<AttributesGlobal>>() {
            @Override
            public void onResponse(@NotNull Call<ArrayList<AttributesGlobal>> call, @NotNull Response<ArrayList<AttributesGlobal>> response) {
                try {
                    if (response.isSuccessful()) {
                        ArrayList<AttributesGlobal> global = response.body();
                        listFound.addAll(global);
                    }
                    listGlobal.postValue(listFound);
                } catch (Exception e) {
                    Log.d(TAG, LOG + "exception:" + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<ArrayList<AttributesGlobal>> call, @NotNull Throwable t) {
                Log.d(TAG, LOG + "failure:" + t.getMessage());
            }
        });
    }
}
