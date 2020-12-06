package com.ariefzuhri.amigo19.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.ariefzuhri.amigo19.utils.CurrentLocation;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.interfaces.LoadUserPreferenceCallback;
import com.ariefzuhri.amigo19.preference.UserFirestore;
import com.ariefzuhri.amigo19.preference.model.UserPreference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ornach.nobobutton.NoboButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.ariefzuhri.amigo19.utils.AppUtils.getTransparentNotifBar;
import static com.ariefzuhri.amigo19.utils.AppUtils.showSnackbar;
import static com.ariefzuhri.amigo19.activity.CustomLocationActivity.EXTRA_CUSTOM_LOCATION;
import static com.ariefzuhri.amigo19.activity.CustomLocationActivity.RC_CUSTOM_LOCATION;

public class SettingLocationActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.RationaleCallbacks, EasyPermissions.PermissionCallbacks {
    private static final int RC_LOCATION_PERM = 123;

    private SwitchCompat switchSetting;
    private NoboButton btnCustomLocation;
    private TextView tvLocation;
    private UserFirestore userFirestore;
    private CurrentLocation currentLocation;

    private String prevLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_location);

        // Atur action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        getTransparentNotifBar(this);

        // Initialize
        userFirestore = new UserFirestore();
        currentLocation = new CurrentLocation(this);
        FloatingActionButton btnBack = findViewById(R.id.btn_back_sla);
        switchSetting = findViewById(R.id.switch_setting_sla);
        btnCustomLocation = findViewById(R.id.btn_custom_location_sla);
        tvLocation = findViewById(R.id.tv_location_sla);

        btnBack.setOnClickListener(this);
        btnCustomLocation.setOnClickListener(this);

        // Muat preference
        userFirestore.fetchPreference(new LoadUserPreferenceCallback() {
            @Override
            public void onCallback(UserPreference userPreference) {
                tvLocation.setText(userPreference.getLocation());
                switchSetting.setChecked(!userPreference.getIsCustomLocation());
                setCustomLocationView(switchSetting.isChecked());
                prevLoc = userPreference.getLocation();
            }
        });

        // Atur view
        switchSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                locationTask();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back_sla:
                onBackPressed();
                break;

            case R.id.btn_custom_location_sla:
                Intent intent = new Intent(SettingLocationActivity.this, CustomLocationActivity.class);
                startActivityForResult(intent, RC_CUSTOM_LOCATION);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_CUSTOM_LOCATION && resultCode == RC_CUSTOM_LOCATION){
            if (data != null){
                tvLocation.setText(data.getStringExtra(EXTRA_CUSTOM_LOCATION));
                savePreference();
            }
        }
    }

    private void setCustomLocationView(boolean isChecked){
        if (isChecked) btnCustomLocation.setEnabled(false);
        else btnCustomLocation.setEnabled(true);
    }

    public void savePreference() {
        /**/
        boolean isAbroad = true;
        String location = tvLocation.getText().toString();
        if (location.equals("-")) isAbroad = false;
        else {
            String[] provinces = getResources().getStringArray(R.array.provinces);
            for (String province : provinces){
                if (province.equals(location)) {
                    isAbroad = false;
                    break;
                }
            }
        }
        /**/

        userFirestore.savePreference(!switchSetting.isChecked(), tvLocation.getText().toString(), isAbroad);
    }

    @Override
    public void onBackPressed(){
        String currLoc = tvLocation.getText().toString();
        if (!currLoc.equals("-")){ // Agar user yang belum atur lokasi, tidak bisa kembali (juga mencegah user kembali saat masih memuat)
            if (currLoc.equals(prevLoc)) super.onBackPressed();
            else{
                Intent intent = new Intent(SettingLocationActivity.this, SplashActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        } else showSnackbar(findViewById(R.id.activity_setting_location), getString(R.string.snackbar_no_location_sla));
    }

    /* EasyPermissions */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private boolean hasLocationPermissions(){
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @AfterPermissionGranted(RC_LOCATION_PERM)
    private void locationTask(){
        if (hasLocationPermissions()){
            // Have permissions, do the thing!
            if (!currentLocation.getState().equals("-")){
                setCustomLocationView(switchSetting.isChecked());
                tvLocation.setText(currentLocation.getState());
            } else {
                showSnackbar(findViewById(R.id.activity_setting_location), getString(R.string.snackbar_fail_location));
                switchSetting.setChecked(false);
            }
            savePreference();
        } else {
            // Ask for permissions
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_location),
                    RC_LOCATION_PERM,
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        /*Saat izin sudah diberikan, lokasi tidak mau terdeteksi
         * Harus di-restart dulu baru mau*/
        if (requestCode == RC_LOCATION_PERM) recreate();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (requestCode == RC_LOCATION_PERM){
            showSnackbar(findViewById(R.id.activity_setting_location), getString(R.string.snackbar_fail_location));
            switchSetting.setChecked(false);
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {}

    @Override
    public void onRationaleDenied(int requestCode) {
        if (requestCode == RC_LOCATION_PERM){
            showSnackbar(findViewById(R.id.activity_setting_location), getString(R.string.snackbar_fail_location));
            switchSetting.setChecked(false);
        }
    }
}
