package com.ariefzuhri.amigo19.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.ariefzuhri.amigo19.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ornach.nobobutton.NoboButton;

import static com.ariefzuhri.amigo19.utils.AppUtils.getFirstName;
import static com.ariefzuhri.amigo19.utils.AppUtils.getTransparentNotifBar;

public class PreAssessmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_assessment);

        // Atur status bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        getTransparentNotifBar(this);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        TextView tvTitle = findViewById(R.id.tv_title_pre_assessment);
        tvTitle.setText(getString(R.string.title_pre_assessment, getFirstName(firebaseUser.getDisplayName())));

        NoboButton btnStart = findViewById(R.id.btn_start_pre_assessment);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PreAssessmentActivity.this, AssessmentActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
