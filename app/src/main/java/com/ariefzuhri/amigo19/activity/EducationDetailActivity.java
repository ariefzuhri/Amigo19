package com.ariefzuhri.amigo19.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.model.EducationDetail;
import com.ariefzuhri.amigo19.pageradapter.EducationDetailPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import static com.ariefzuhri.amigo19.utils.AppUtils.getTransparentNotifBar;

public class EducationDetailActivity extends AppCompatActivity {
    public static final String EXTRA_INDEX_EDUCATION = "extra_index_education";
    private ArrayList<EducationDetail> educationDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_detail);

        // Atur action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        getTransparentNotifBar(this);

        // Initialize
        TextView tvTopic = findViewById(R.id.tv_title_education_detail);
        FloatingActionButton btnBack = findViewById(R.id.btn_back_education_detail);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Muat array
        String[] topic = getResources().getStringArray(R.array.topic_education);
        @SuppressLint("Recycle") TypedArray image = getResources().obtainTypedArray(R.array.image_education);
        String[] content = getResources().getStringArray(R.array.content_education);

        // Muat index dan judul
        int index = getIntent().getIntExtra(EXTRA_INDEX_EDUCATION, -1);
        tvTopic.setText(topic[index]);

        // Masukkan data ke list sesuai index
        educationDetails.add(new EducationDetail(image.getResourceId(index*3, 0), content[index*3]));
        educationDetails.add(new EducationDetail(image.getResourceId(index*3+1, 0), content[index*3+1]));
        educationDetails.add(new EducationDetail(image.getResourceId(index*3+2, 0), content[index*3+2]));

        // Masukkan list data ke adapter
        EducationDetailPagerAdapter pagerAdapter = new EducationDetailPagerAdapter(educationDetails, this);

        // Atur view pager
        ViewPager viewPager = findViewById(R.id.view_pager_education_detail);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPadding(130, 0, 130, 0);
    }
}
