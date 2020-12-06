package com.ariefzuhri.amigo19.utils;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import androidx.annotation.NonNull;

import com.ariefzuhri.amigo19.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.ariefzuhri.amigo19.utils.AppUtils.LOG;

public class CurrentLocation {
    private final static String TAG = "CurrentLocation";

    private Activity activity;
    private double latitude, longitude;
    private String subThoroughfare; // Gang
    private String thoroughfare; // Jalan
    private String subDistrict; // Kelurahan
    private String district; // Kecamatan
    private String city; // Kota atau Kabupaten
    private String state; // Provinsi
    private String country; // Negara
    private String zipCode; // Kode pos
    private String countryCode; // Kode negara
    private String fullAddress; // Alamat lengkap

    public CurrentLocation(Activity activity){
        this.activity = activity;
        latitude = 0;
        longitude = 0;
        subThoroughfare = "-";
        thoroughfare = "-";
        subDistrict = "-";
        district = "-";
        city = "-";
        state = "-";
        country = "-";
        zipCode = "-";
        countryCode = "-";
        fullAddress = "-";
        getCurrentLocation();
    }

    private void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    private void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private void getCurrentLocation() {
        // Get coordinate first
        FusedLocationProviderClient providerClient = LocationServices.getFusedLocationProviderClient(activity);
        providerClient.getLastLocation().addOnCompleteListener(activity, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        setLatitude(task.getResult().getLatitude());
                        setLongitude(task.getResult().getLongitude());
                        Log.d(TAG, LOG + "success: lat:" + latitude + " long:" + longitude);

                        getLocationName();
                    } else {
                        Log.d(TAG, LOG + "failure:null coor");
                    }
                } else {
                    Log.d(TAG, LOG + "failure:task failed");
                }
            }
        });
    }

    private void getLocationName(){
        Locale locale = new Locale.Builder().setLanguage("in").setScript("Latn").setRegion("ID").build();
        Geocoder geocoder = new Geocoder(activity, locale);

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 2);
            if (addresses.size() > 1){ // index 0 dalam English, 1 bahasa
                subThoroughfare = addresses.get(1).getSubThoroughfare();
                thoroughfare = addresses.get(1).getThoroughfare();
                subDistrict = addresses.get(1).getSubLocality();
                district = addresses.get(1).getLocality();
                city = addresses.get(1).getSubAdminArea();
                state = addresses.get(1).getAdminArea();
                country = addresses.get(1).getCountryName();
                zipCode = addresses.get(1).getPostalCode();
                countryCode = addresses.get(1).getCountryCode();
                fullAddress = addresses.get(1).getAddressLine(0);

                Log.d(TAG, LOG + "success: loc:" + fullAddress);
            } else {
                Log.d(TAG, LOG + "failure: loc:" + fullAddress);
            }
        } catch (IOException e) {
            Log.d(TAG, LOG + "failure: loc:" + e);
        }
    }

    // Ambil nama provinsi jika di Indonesia
    // Ambil nama negara jika di luar negeri
    public String getState() {
        if (country.equals(activity.getString(R.string.indonesia_statistic))) return state;
        else return country;
    }
}
