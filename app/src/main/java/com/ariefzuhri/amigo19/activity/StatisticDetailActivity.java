package com.ariefzuhri.amigo19.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.ariefzuhri.amigo19.customview.LoadingDialog;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.adapter.StatisticAdapter;
import com.ariefzuhri.amigo19.response.AttributesGlobal;
import com.ariefzuhri.amigo19.response.AttributesProvince;
import com.ariefzuhri.amigo19.viewmodel.GlobalViewModel;
import com.ariefzuhri.amigo19.viewmodel.IndonesiaProvincesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import static com.ariefzuhri.amigo19.utils.AppUtils.getTransparentNotifBar;

public class StatisticDetailActivity extends AppCompatActivity {
    private StatisticAdapter adapter;
    public static final String EXTRA_TITLE = "extra_title";
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        getTransparentNotifBar(this);

        loadingDialog = new LoadingDialog(this);

        TextView tvTitle = findViewById(R.id.tv_title_statistic_detail);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        tvTitle.setText(getString(R.string.title_statistic_detail, title));

        RecyclerView recyclerView = findViewById(R.id.rv_statistic_detail);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StatisticAdapter(this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        loadingDialog.startDialog();
        if (title.equals(getString(R.string.indonesia_statistic))){
            IndonesiaProvincesViewModel ipvm = new ViewModelProvider(StatisticDetailActivity.this, new ViewModelProvider.NewInstanceFactory()).get(IndonesiaProvincesViewModel.class);
            ipvm.setIndonesiaProvinces();
            ipvm.getIndonesiaProvinces().observe(StatisticDetailActivity.this, new Observer<ArrayList<AttributesProvince>>() {
                @Override
                public void onChanged(ArrayList<AttributesProvince> indonesiaProvinces) {
                    if (indonesiaProvinces != null) adapter.setDataIndonesia(indonesiaProvinces);
                    loadingDialog.dismissDialog();
                }
            });
        } else if (title.equals(getString(R.string.global_statistic))){
            GlobalViewModel gvm = new ViewModelProvider(StatisticDetailActivity.this, new ViewModelProvider.NewInstanceFactory()).get(GlobalViewModel.class);
            gvm.setGlobal();
            gvm.getGlobal().observe(StatisticDetailActivity.this, new Observer<ArrayList<AttributesGlobal>>() {
                @Override
                public void onChanged(ArrayList<AttributesGlobal> global) {
                    if (global != null) adapter.setDataGlobal(global);
                    loadingDialog.dismissDialog();
                }
            });
        }

        FloatingActionButton btnBack = findViewById(R.id.btn_back_statistic_detail);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
