package com.ariefzuhri.amigo19.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ariefzuhri.amigo19.BuildConfig;
import com.ariefzuhri.amigo19.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import static com.ariefzuhri.amigo19.utils.AppUtils.getTransparentNotifBar;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        getTransparentNotifBar(this);

        TextView tvVersion = findViewById(R.id.tv_version_about);
        tvVersion.setText(getString(R.string.app_version, BuildConfig.VERSION_NAME));

        FloatingActionButton btnBack = findViewById(R.id.btn_back_about);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
