package com.ariefzuhri.amigo19.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.interfaces.LoadAssessmentPreferenceCallback;
import com.ariefzuhri.amigo19.preference.AssessmentFirestore;
import com.ariefzuhri.amigo19.preference.model.AssessmentPreference;
import com.ariefzuhri.amigo19.localpreference.FirstTimeLaunchPreference;
import com.google.firebase.auth.FirebaseAuth;
import static com.ariefzuhri.amigo19.utils.AppUtils.getTransparentNotifBar;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Atur status bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        getTransparentNotifBar(this);

        // Initialize
        firebaseAuth = FirebaseAuth.getInstance();

        // Jeda
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firebaseAuth.getCurrentUser() != null) launchAssessmentOrMain();
                else launchIntroOrLogin();
            }
        }, 500);
    }

    /*
     * Untuk yang sudah login
     * - User blm asesmen -> PreAssessmentActivity
     * - User sdh asesmen -> MainActivity*/
    private void launchAssessmentOrMain(){
        AssessmentFirestore assessmentFirestore = new AssessmentFirestore(); // Jangan di atas, karena kalo diinisiasi auto cari id, dan bisa jadi lg log out
        assessmentFirestore.fetchPreference(new LoadAssessmentPreferenceCallback() {
            @Override
            public void onCallback(AssessmentPreference assessmentPreference) {
                Intent intent;
                if (assessmentPreference.getIsFillOut()) intent = new Intent(SplashActivity.this, MainActivity.class);
                else intent = new Intent(SplashActivity.this, PreAssessmentActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /*
    * Untuk yang belum login
    * - User baru atau user lama setelah reinstall aplikasi -> IntroActivity -> LoginActivity
    * - User lama yang log out -> LoginActivity*/
    private void launchIntroOrLogin(){
        FirstTimeLaunchPreference firstTimeLaunchPreference = new FirstTimeLaunchPreference(this);
        Intent intent;
        if (firstTimeLaunchPreference.isFirstTimeLaunch()) intent = new Intent(SplashActivity.this, IntroActivity.class);
        else intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
