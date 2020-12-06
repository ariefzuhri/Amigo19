package com.ariefzuhri.amigo19.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.ariefzuhri.amigo19.response.GlobalSummary;
import com.ariefzuhri.amigo19.rest.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ariefzuhri.amigo19.utils.AppUtils.LOG;

public class GlobalSummaryViewModel extends ViewModel {
    private static final String TAG = "GlobalSummaryViewModel";

    private MutableLiveData<GlobalSummary> globalInfected  = new MutableLiveData<>();
    private MutableLiveData<GlobalSummary> globalRecovered  = new MutableLiveData<>();
    private MutableLiveData<GlobalSummary> globalDeath  = new MutableLiveData<>();

    public LiveData<GlobalSummary> getGlobalInfected() {
        return globalInfected;
    }
    public LiveData<GlobalSummary> getGlobalRecovered() {
        return globalRecovered;
    }
    public LiveData<GlobalSummary> getGlobalDeath() {
        return globalDeath;
    }

    public void setGlobalInfected() {
        RetrofitClient client = new RetrofitClient();
        client.getService().getGlobalInfected().enqueue(new Callback<GlobalSummary>() {
            @Override
            public void onResponse(@NotNull Call<GlobalSummary> call, @NotNull Response<GlobalSummary> response) {
                try {
                    if (response.isSuccessful()) globalInfected.postValue(response.body());
                } catch (Exception e) {
                    Log.d(TAG, LOG + "exception:" + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<GlobalSummary> call, @NotNull Throwable t) {
                Log.d(TAG, LOG + "failure:" + t.getMessage());
            }
        });
    }

    public void setGlobalDeath() {
        RetrofitClient client = new RetrofitClient();
        client.getService().getGlobalDeath().enqueue(new Callback<GlobalSummary>() {
            @Override
            public void onResponse(@NotNull Call<GlobalSummary> call, @NotNull Response<GlobalSummary> response) {
                try {
                    if (response.isSuccessful()) globalDeath.postValue(response.body());
                } catch (Exception e) {
                    Log.d(TAG, LOG + "exception:" + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<GlobalSummary> call, @NotNull Throwable t) {
                Log.d(TAG, LOG + "failure:" + t.getMessage());
            }
        });
    }

    public void setGlobalRecovered() {
        RetrofitClient client = new RetrofitClient();
        client.getService().getGlobalRecovered().enqueue(new Callback<GlobalSummary>() {
            @Override
            public void onResponse(@NotNull Call<GlobalSummary> call, @NotNull Response<GlobalSummary> response) {
                try {
                    if (response.isSuccessful()) globalRecovered.postValue(response.body());
                } catch (Exception e){
                    Log.d(TAG, LOG + "exception:" + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call<GlobalSummary> call, @NotNull Throwable t) {
                Log.d(TAG, LOG + "failure:" + t.getMessage());
            }
        });
    }
}
