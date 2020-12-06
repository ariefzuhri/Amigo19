package com.ariefzuhri.amigo19.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.ariefzuhri.amigo19.customview.LoadingDialog;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.adapter.CustomLocationAdapter;
import com.ariefzuhri.amigo19.interfaces.LoadUserPreferenceCallback;
import com.ariefzuhri.amigo19.model.Region;
import com.ariefzuhri.amigo19.preference.UserFirestore;
import com.ariefzuhri.amigo19.preference.model.UserPreference;
import com.ariefzuhri.amigo19.response.WCUCountries;
import com.ariefzuhri.amigo19.response.WCUCountry;
import com.ariefzuhri.amigo19.viewmodel.WorldCountriesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import static com.ariefzuhri.amigo19.utils.AppUtils.getTransparentNotifBar;

public class CustomLocationActivity extends AppCompatActivity {
    public final static int RC_CUSTOM_LOCATION = 101;
    public final static String EXTRA_CUSTOM_LOCATION = "extra_custom_location";

    private CustomLocationAdapter adapter;
    private ArrayList<Region> listRegions = new ArrayList<>();
    private LoadingDialog loadingDialog;
    private SwitchCompat switchAbroad;
    private TextView tvTitle;
    private WorldCountriesViewModel wcvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_location);

        // Initialize
        UserFirestore userFirestore = new UserFirestore();
        switchAbroad = findViewById(R.id.switch_abroad_cla);
        tvTitle = findViewById(R.id.tv_title_cla);

        wcvm = new ViewModelProvider(CustomLocationActivity.this, new ViewModelProvider.NewInstanceFactory()).get(WorldCountriesViewModel.class);

        // Atur action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        getTransparentNotifBar(this);

        // Atur recycler view
        RecyclerView recyclerView = findViewById(R.id.rv_cla);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomLocationAdapter(this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        // Atur tombol
        FloatingActionButton btnBack = findViewById(R.id.btn_back_cla);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Atur search filter
        SearchView searchBar = findViewById(R.id.search_bar_cla);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        // Atur tampilan
        loadView();
        userFirestore.fetchPreference(new LoadUserPreferenceCallback() {
            @Override
            public void onCallback(UserPreference userPreference) {
                switchAbroad.setChecked(userPreference.getIsAbroad()); // pertama kali
                switchAbroad.getOnFocusChangeListener();
            }
        });

        // Atur switch, taruh bawah, jaga-jaga biar gak load 2x
        switchAbroad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                loadView();
            }
        });
    }

    public void loadView(){
        if (switchAbroad.isChecked()) tvTitle.setText(getString(R.string.title_cla, getString(R.string.state).toLowerCase()));
        else tvTitle.setText(getString(R.string.title_cla, getString(R.string.province).toLowerCase()));
        loadList(switchAbroad.isChecked()); // Muat daftar provinsi/negara
    }

    private void loadList(boolean isAbroad) {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.startDialog();
        listRegions.clear(); // Clear untuk keduanya

        if (isAbroad){
            wcvm.setWorldCountries();
            wcvm.getWorldCountries().observe(CustomLocationActivity.this, new Observer<WCUCountries>() {
                @Override
                public void onChanged(WCUCountries countries) {
                    if (countries != null) {
                        listRegions.clear(); // Clear-nya juga harus di sini, kalo gak ttp dobel2
                        for (WCUCountry country : countries.getCountries()) {
                            if (!country.getName().equals(getString(R.string.indonesia_statistic))){
                                Region region = new Region(country.getName(), country.getIso2());
                                listRegions.add(region);
                            }
                        }
                        adapter.setData(listRegions);
                        loadingDialog.dismissDialog();
                    }
                }
            });
        } else {
            String[] provinces = getResources().getStringArray(R.array.provinces);
            for (int i = 0; i < provinces.length; i++) listRegions.add(new Region(i+1, provinces[i]));
            adapter.setData(listRegions);
            loadingDialog.dismissDialog();
        }
    }
}
