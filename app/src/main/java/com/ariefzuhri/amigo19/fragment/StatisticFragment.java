package com.ariefzuhri.amigo19.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.activity.StatisticDetailActivity;
import com.ariefzuhri.amigo19.interfaces.LoadUserPreferenceCallback;
import com.ariefzuhri.amigo19.model.Statistic;
import com.ariefzuhri.amigo19.preference.UserFirestore;
import com.ariefzuhri.amigo19.preference.model.UserPreference;
import com.ariefzuhri.amigo19.response.AttributesGlobal;
import com.ariefzuhri.amigo19.response.AttributesProvince;
import com.ariefzuhri.amigo19.response.Country;
import com.ariefzuhri.amigo19.response.GlobalSummary;
import com.ariefzuhri.amigo19.viewmodel.GlobalViewModel;
import com.ariefzuhri.amigo19.viewmodel.IndonesiaProvincesViewModel;
import com.ariefzuhri.amigo19.viewmodel.GlobalSummaryViewModel;
import com.ariefzuhri.amigo19.viewmodel.IndonesiaViewModel;
import java.util.ArrayList;

import static com.ariefzuhri.amigo19.utils.AppUtils.getDecimalFormat;
import static com.ariefzuhri.amigo19.activity.StatisticDetailActivity.EXTRA_TITLE;
import static com.ariefzuhri.amigo19.utils.DateUtils.getCurrentTime;

public class StatisticFragment extends Fragment implements View.OnClickListener {
    private TextView tvInfectedProvince, tvRecoveredProvince, tvDeathProvince, tvPatientProvince;
    private TextView tvInfectedCountry, tvRecoveredCountry, tvDeathCountry, tvPatientCountry;
    private TextView tvInfectedGlobal, tvRecoveredGlobal, tvDeathGlobal, tvPatientGlobal;
    private TextView tvLastUpdate, tvTitleProvince;
    private Statistic globalSummaryStatistic, userLocationStatistic;
    private IndonesiaProvincesViewModel ipvm;
    private GlobalViewModel gvm;
    private IndonesiaViewModel ivm;
    private Context context;

    public StatisticFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState){
        // Initialize
        context = getActivity();
        UserFirestore userFirestore = new UserFirestore();
        CardView cvProvince = view.findViewById(R.id.cv_province_statistic);
        CardView cvCountry = view.findViewById(R.id.cv_country_statistic);
        CardView cvGlobal = view.findViewById(R.id.cv_global_statistic);
        tvInfectedProvince = view.findViewById(R.id.tv_infected_province_statistic);
        tvRecoveredProvince = view.findViewById(R.id.tv_recovered_province_statistic);
        tvDeathProvince = view.findViewById(R.id.tv_death_province_statistic);
        tvPatientProvince = view.findViewById(R.id.tv_patient_province_statistic);
        tvInfectedCountry = view.findViewById(R.id.tv_infected_country_statistic);
        tvRecoveredCountry = view.findViewById(R.id.tv_recovered_country_statistic);
        tvDeathCountry = view.findViewById(R.id.tv_death_country_statistic);
        tvPatientCountry = view.findViewById(R.id.tv_patient_country_statistic);
        tvInfectedGlobal = view.findViewById(R.id.tv_infected_global_statistic);
        tvRecoveredGlobal = view.findViewById(R.id.tv_recovered_global_statistic);
        tvDeathGlobal = view.findViewById(R.id.tv_death_global_statistic);
        tvPatientGlobal = view.findViewById(R.id.tv_patient_global_statistic);
        tvLastUpdate = view.findViewById(R.id.tv_last_update_statistic);
        tvTitleProvince = view.findViewById(R.id.tv_name_province_statistic);
        ImageButton btnInfo = view.findViewById(R.id.btn_info_statistic);

        // Buat view model
        ipvm = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(IndonesiaProvincesViewModel.class);
        gvm = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(GlobalViewModel.class);
        ivm = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(IndonesiaViewModel.class);

        // Atur listener
        cvProvince.setOnClickListener(this);
        cvCountry.setOnClickListener(this);
        cvGlobal.setOnClickListener(this);
        btnInfo.setOnClickListener(this);

        // Muat tampilan statistik
        loadIndonesiaStatistic();
        loadGlobalStatistic();
        userFirestore.fetchPreference(new LoadUserPreferenceCallback() {
            @Override
            public void onCallback(UserPreference userPreference) {
                loadUserLocationStatistic(userPreference);
                tvLastUpdate.setText(getActivity().getString(R.string.last_update_statistic, getCurrentTime()));
            }
        });
    }

    private void loadUserLocationStatistic(final UserPreference userPreference) {
        tvTitleProvince.setText(userPreference.getLocation());

        if (!userPreference.getIsAbroad()){
            ipvm.setIndonesiaProvinces();
            ipvm.getIndonesiaProvinces().observe(getViewLifecycleOwner(), new Observer<ArrayList<AttributesProvince>>() {
                @Override
                public void onChanged(ArrayList<AttributesProvince> indonesiaProvinces) {
                    if (indonesiaProvinces != null){
                        for (AttributesProvince province : indonesiaProvinces){
                            if (province.getProvince().getName().equals(userPreference.getLocation())){
                                userLocationStatistic = new Statistic(province.getProvince().getId(), province.getProvince().getName(), province.getProvince().getInfected(), province.getProvince().getRecovered(), province.getProvince().getDeath());
                                tvInfectedProvince.setText(Html.fromHtml(getActivity().getString(R.string.stats_infected, getDecimalFormat(userLocationStatistic.getInfected()))));
                                tvRecoveredProvince.setText(Html.fromHtml(getActivity().getString(R.string.stats_recovered, getDecimalFormat(userLocationStatistic.getRecovered()))));
                                tvDeathProvince.setText(Html.fromHtml(getActivity().getString(R.string.stats_death, getDecimalFormat(userLocationStatistic.getDeath()))));
                                tvPatientProvince.setText(Html.fromHtml(getActivity().getString(R.string.stats_patient, getDecimalFormat(userLocationStatistic.getPatient()))));
                                break;
                            }
                        }
                    }
                }
            });
        } else {
            gvm.setGlobal();
            gvm.getGlobal().observe(getViewLifecycleOwner(), new Observer<ArrayList<AttributesGlobal>>() {
                @Override
                public void onChanged(ArrayList<AttributesGlobal> global) {
                    if (global != null){
                        for (AttributesGlobal country : global){
                            if (country.getGlobal().getName().equals(userPreference.getLocation())){
                                userLocationStatistic = new Statistic(country.getGlobal().getName(), country.getGlobal().getInfected(), country.getGlobal().getRecovered(), country.getGlobal().getDeath());
                                tvInfectedProvince.setText(Html.fromHtml(getActivity().getString(R.string.stats_infected, getDecimalFormat(userLocationStatistic.getInfected()))));
                                tvRecoveredProvince.setText(Html.fromHtml(getActivity().getString(R.string.stats_recovered, getDecimalFormat(userLocationStatistic.getRecovered()))));
                                tvDeathProvince.setText(Html.fromHtml(getActivity().getString(R.string.stats_death, getDecimalFormat(userLocationStatistic.getDeath()))));
                                tvPatientProvince.setText(Html.fromHtml(getActivity().getString(R.string.stats_patient, getDecimalFormat(userLocationStatistic.getPatient()))));
                                break;
                            }
                        }
                    }
                }
            });
        }
    }

    private void loadIndonesiaStatistic(){
        ivm.setIndonesia();
        ivm.getIndonesia().observe(getViewLifecycleOwner(), new Observer<ArrayList<Country>>() {
            @Override
            public void onChanged(ArrayList<Country> indonesia) {
                if (indonesia != null){
                    tvInfectedCountry.setText(Html.fromHtml(getActivity().getString(R.string.stats_infected, getDecimalFormat(indonesia.get(0).getInfected()))));
                    tvRecoveredCountry.setText(Html.fromHtml(getActivity().getString(R.string.stats_recovered, getDecimalFormat(indonesia.get(0).getRecovered()))));
                    tvDeathCountry.setText(Html.fromHtml(getActivity().getString(R.string.stats_death, getDecimalFormat(indonesia.get(0).getDeath()))));
                    tvPatientCountry.setText(Html.fromHtml(getActivity().getString(R.string.stats_patient, getDecimalFormat(indonesia.get(0).getPatient()))));
                }
            }
        });
    }

    private void loadGlobalStatistic(){
        globalSummaryStatistic = new Statistic(getActivity().getString(R.string.global_statistic));
        final GlobalSummaryViewModel gsvm = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(GlobalSummaryViewModel.class);

        gsvm.setGlobalInfected();
        gsvm.getGlobalInfected().observe(getViewLifecycleOwner(), new Observer<GlobalSummary>() {
            @Override
            public void onChanged(GlobalSummary globalInfected) {
                if (globalInfected != null){
                    globalSummaryStatistic.setInfected(globalInfected.getCount());
                    gsvm.setGlobalRecovered();
                }
            }
        });
        gsvm.getGlobalRecovered().observe(getViewLifecycleOwner(), new Observer<GlobalSummary>() {
            @Override
            public void onChanged(GlobalSummary globalRecovered) {
                if (globalRecovered != null){
                    globalSummaryStatistic.setRecovered(globalRecovered.getCount());
                    gsvm.setGlobalDeath();
                }
            }
        });
        gsvm.getGlobalDeath().observe(getViewLifecycleOwner(), new Observer<GlobalSummary>() {
            @Override
            public void onChanged(GlobalSummary globalDeath) {
                if (globalDeath != null){
                    globalSummaryStatistic.setDeath(globalDeath.getCount());
                    tvInfectedGlobal.setText(Html.fromHtml(getActivity().getString(R.string.stats_infected, getDecimalFormat(globalSummaryStatistic.getInfected()))));
                    tvRecoveredGlobal.setText(Html.fromHtml(getActivity().getString(R.string.stats_recovered, getDecimalFormat(globalSummaryStatistic.getRecovered()))));
                    tvDeathGlobal.setText(Html.fromHtml(getActivity().getString(R.string.stats_death, getDecimalFormat(globalSummaryStatistic.getDeath()))));
                    tvPatientGlobal.setText(Html.fromHtml(getActivity().getString(R.string.stats_patient, getDecimalFormat(globalSummaryStatistic.getPatient()))));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_info_statistic:
                AlertDialog.Builder dialogInfo = new AlertDialog.Builder(context);
                dialogInfo.setMessage(R.string.dialog_info_statistic);
                dialogInfo.setNeutralButton(R.string.dialog_ok, null);
                dialogInfo.create().show();
                break;

            case R.id.cv_province_statistic:
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setNeutralButton(R.string.dialog_ok, null);
                if (userLocationStatistic != null){
                    dialog.setMessage(getActivity().getString(R.string.dialog_province_statistic,
                            userLocationStatistic.getName(),
                            getDecimalFormat(userLocationStatistic.getInfected()),
                            getDecimalFormat(userLocationStatistic.getPatient()),
                            getDecimalFormat(userLocationStatistic.getRecovered()),
                            getDecimalFormat(userLocationStatistic.getDeath())));
                } else dialog.setMessage(R.string.dialog_no_data);
                dialog.create().show();
                break;

            case R.id.cv_country_statistic:
                Intent intentIndonesia = new Intent(getActivity(), StatisticDetailActivity.class);
                intentIndonesia.putExtra(EXTRA_TITLE, getActivity().getString(R.string.indonesia_statistic));
                startActivity(intentIndonesia);
                break;

            case R.id.cv_global_statistic:
                Intent intentGlobal = new Intent(getActivity(), StatisticDetailActivity.class);
                intentGlobal.putExtra(EXTRA_TITLE, getActivity().getString(R.string.global_statistic));
                startActivity(intentGlobal);
                break;
        }
    }

    private static Spanned convertToHtml(Activity activity, int resId, int s1, int s2){
        String inc = getDecimalFormat(s2);
        if (s2 >= 0) inc = "+" + inc;
        else inc = "-" + inc;
        return Html.fromHtml(activity.getString(resId, getDecimalFormat(s1), inc));
    }
}
