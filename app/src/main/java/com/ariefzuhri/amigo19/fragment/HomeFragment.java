package com.ariefzuhri.amigo19.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.customview.UpdateDialog;
import com.ariefzuhri.amigo19.activity.AboutActivity;
import com.ariefzuhri.amigo19.activity.FeedbackActivity;
import com.ariefzuhri.amigo19.activity.MeetDetailActivity;
import com.ariefzuhri.amigo19.activity.SettingLocationActivity;
import com.ariefzuhri.amigo19.activity.ShoppingModeActivity;
import com.ariefzuhri.amigo19.customview.WhatsNewDialog;
import com.ariefzuhri.amigo19.interfaces.LoadAssessmentPreferenceCallback;
import com.ariefzuhri.amigo19.interfaces.LoadUserPreferenceCallback;
import com.ariefzuhri.amigo19.preference.AssessmentFirestore;
import com.ariefzuhri.amigo19.preference.model.AssessmentPreference;
import com.ariefzuhri.amigo19.preference.model.UserPreference;
import com.ariefzuhri.amigo19.preference.UserFirestore;
import com.ariefzuhri.amigo19.response.AttributesGlobal;
import com.ariefzuhri.amigo19.response.AttributesProvince;
import com.ariefzuhri.amigo19.viewmodel.GlobalViewModel;
import com.ariefzuhri.amigo19.viewmodel.IndonesiaProvincesViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ornach.nobobutton.NoboButton;
import java.util.ArrayList;
import java.util.Random;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static com.ariefzuhri.amigo19.utils.AppUtils.getFirstName;
import static com.ariefzuhri.amigo19.utils.DateUtils.differenceOfDates;
import static com.ariefzuhri.amigo19.utils.DateUtils.getCurrentDate;
import static com.ariefzuhri.amigo19.utils.DateUtils.getDayName;
import static com.ariefzuhri.amigo19.utils.AppUtils.getDecimalFormat;
import static com.ariefzuhri.amigo19.utils.AppUtils.getGreetings;
import static com.ariefzuhri.amigo19.utils.DateUtils.getFullDate;
import static com.ariefzuhri.amigo19.utils.AppUtils.showSnackbar;
import static com.ariefzuhri.amigo19.utils.AssessmentUtils.getNextMeet;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private TextView tvTitle, tvMotivation, tvCountdown, tvLocation, tvDateNextOverview, tvDateLastOverview;
    private TextView tvInfected, tvRecovered, tvDeath, tvPatient;
    private FirebaseUser user;
    private ProgressBar progressBar; // Kasi satu aja untuk MainActivity, kalo gagal muat
    private AssessmentFirestore assessmentFirestore;
    private String nextMeet;
    private UserFirestore userFirestore;
    private UserPreference userPreference;
    private UpdateDialog updateDialog;
    private WhatsNewDialog whatsNewDialog;
    private Context context;
    private IndonesiaProvincesViewModel ipvm;
    private GlobalViewModel gvm;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        // Initialize
        assessmentFirestore = new AssessmentFirestore();
        userFirestore = new UserFirestore();
        userPreference = new UserPreference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        updateDialog = new UpdateDialog(getActivity());
        whatsNewDialog = new WhatsNewDialog(getActivity());
        context = getActivity();

        progressBar = view.findViewById(R.id.progress_bar_home);
        NoboButton btnShoppingMode = view.findViewById(R.id.btn_shopping_mode_home);
        NoboButton btnMeetDetail = view.findViewById(R.id.btn_meet_detail_home);
        ImageButton btnMenu = view.findViewById(R.id.btn_menu_home);
        ImageButton btnInfoOverview = view.findViewById(R.id.btn_info_overview_home);
        ImageButton btnInfoShoppingMode = view.findViewById(R.id.btn_info_shopping_mode_home);
        CardView cvDateNext = view.findViewById(R.id.cv_date_next_overview_home);
        CardView cvDateLast = view.findViewById(R.id.cv_date_last_overview_home);
        tvTitle = view.findViewById(R.id.tv_title_home);
        tvMotivation = view.findViewById(R.id.tv_motivation_home);
        tvCountdown = view.findViewById(R.id.tv_countdown_home);
        tvDateNextOverview = view.findViewById(R.id.tv_date_next_overview_home);
        tvDateLastOverview = view.findViewById(R.id.tv_date_last_overview_home);
        tvLocation = view.findViewById(R.id.tv_location_home);
        tvInfected = view.findViewById(R.id.tv_count_infected_statistic_home);
        tvRecovered = view.findViewById(R.id.tv_count_recovered_statistic_home);
        tvDeath = view.findViewById(R.id.tv_count_death_statistic_home);
        tvPatient = view.findViewById(R.id.tv_count_patient_statistic_home);

        // Buat view model
        ipvm = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(IndonesiaProvincesViewModel.class);
        gvm = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(GlobalViewModel.class);

        loadView();

        // Pasang listener
        btnMenu.setOnClickListener(this);
        cvDateNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage(R.string.dialog_next_meet);
                dialog.setNegativeButton(R.string.dialog_undone, null);
                dialog.setPositiveButton(R.string.dialog_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        assessmentFirestore.savePreference(getCurrentDate());
                        loadDateOverview();
                    }
                });

                if (differenceOfDates(nextMeet, getCurrentDate())==0){
                    dialog.setNeutralButton(R.string.dialog_delay, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            assessmentFirestore.fetchPreference(new LoadAssessmentPreferenceCallback() {
                                @Override
                                public void onCallback(AssessmentPreference assessmentPreference) {
                                    assessmentFirestore.savePreference(assessmentPreference.getNumberOfDelay()+1);
                                    loadDateOverview();
                                }
                            });
                        }
                    });
                }

                dialog.create().show();
            }
        });

        cvDateLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assessmentFirestore.fetchPreference(new LoadAssessmentPreferenceCallback() {
                    @Override
                    public void onCallback(AssessmentPreference assessmentPreference) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        if (!assessmentPreference.getLastMeet().equals("-")){ // Jika sudah pernah belanja
                            dialog.setMessage(getActivity().getString(R.string.dialog_last_meet,
                                            tvDateLastOverview.getText().toString(),
                                            differenceOfDates(getCurrentDate(), assessmentPreference.getLastMeet())));
                            dialog.setNeutralButton(R.string.dialog_ok, null);
                        } else {
                            dialog.setMessage(R.string.dialog_no_data);
                            dialog.setNeutralButton(R.string.dialog_ok, null);
                        }
                        dialog.create().show();
                    }
                });
            }
        });

        btnShoppingMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage(R.string.dialog_start_shopping_mode);
                dialog.setNegativeButton(R.string.dialog_cancel, null);
                dialog.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), ShoppingModeActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        });
                dialog.create().show();
            }
        });

        btnMeetDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MeetDetailActivity.class);
                startActivity(intent);
            }
        });

        btnInfoOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage(R.string.dialog_info_overview_home);
                dialog.setNeutralButton(R.string.dialog_understand, null);
                dialog.create().show();
            }
        });

        btnInfoShoppingMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage(R.string.dialog_info_shopping_mode_home);
                dialog.setNeutralButton(R.string.dialog_understand, null);
                dialog.create().show();
            }
        });
    }

    @Override
    // Update realtime tampilan jika terjadi perubahan setelan
    public void onStart() {
        super.onStart();
        if (getActivity() != null){
            if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
                showSnackbar(getActivity().findViewById(R.id.fragment_home), getString(R.string.snackbar_no_potrait_home));
        }
    }

    private void loadView(){
        loadDateOverview();
        String[] motivation = getActivity().getResources().getStringArray(R.array.motivation);
        tvTitle.setText(getGreetings(getActivity(), getFirstName(user.getDisplayName())));
        tvMotivation.setText(motivation[new Random().nextInt(motivation.length)]);

        userFirestore.fetchPreference(new LoadUserPreferenceCallback() {
            @Override
            public void onCallback(UserPreference preference) {
                if (!preference.getLocation().equals("-")){
                    userPreference = preference;
                    tvLocation.setText(preference.getLocation());
                    loadStatistic();
                }else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setCancelable(false);
                    dialog.setMessage(R.string.dialog_setup_location_home);
                    dialog.setNeutralButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getActivity(), SettingLocationActivity.class);
                                    startActivity(intent);
                                }
                            });
                    dialog.create().show();
                }
            }
        });
    }

    private void loadDateOverview(){
        assessmentFirestore.fetchPreference(new LoadAssessmentPreferenceCallback() {
            @Override
            public void onCallback(AssessmentPreference assessmentPreference) {
                nextMeet = getNextMeet(assessmentFirestore, assessmentPreference);
                tvCountdown.setText(String.valueOf(differenceOfDates(nextMeet, getCurrentDate())));
                tvDateNextOverview.setText(getActivity().getString(R.string.day_full_date, getDayName(nextMeet), getFullDate(nextMeet)));
                tvDateLastOverview.setText(getActivity().getString(R.string.day_full_date, getDayName(assessmentPreference.getLastMeet()), getFullDate(assessmentPreference.getLastMeet())));
            }
        });
    }

    private void loadStatistic(){
        progressBar.setVisibility(View.VISIBLE);

        if (!userPreference.getIsAbroad()){
            ipvm.setIndonesiaProvinces();
            ipvm.getIndonesiaProvinces().observe(getViewLifecycleOwner(), new Observer<ArrayList<AttributesProvince>>() {
                @Override
                public void onChanged(ArrayList<AttributesProvince> indonesiaProvinces) {
                    if (indonesiaProvinces != null){
                        for (AttributesProvince province : indonesiaProvinces){
                            if (province.getProvince().getName().equals(userPreference.getLocation())){
                                tvInfected.setText(getDecimalFormat(province.getProvince().getInfected()));
                                tvRecovered.setText(getDecimalFormat(province.getProvince().getRecovered()));
                                tvDeath.setText(getDecimalFormat(province.getProvince().getDeath()));
                                tvPatient.setText(getDecimalFormat(province.getProvince().getPatient()));
                                break;
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        } else{
            gvm.setGlobal();
            gvm.getGlobal().observe(getViewLifecycleOwner(), new Observer<ArrayList<AttributesGlobal>>() {
                @Override
                public void onChanged(ArrayList<AttributesGlobal> global) {
                    if (global != null){
                        for (AttributesGlobal country : global){
                            if (country.getGlobal().getName().equals(userPreference.getLocation())){
                                tvInfected.setText(getDecimalFormat(country.getGlobal().getInfected()));
                                tvRecovered.setText(getDecimalFormat(country.getGlobal().getRecovered()));
                                tvDeath.setText(getDecimalFormat(country.getGlobal().getDeath()));
                                tvPatient.setText(getDecimalFormat(country.getGlobal().getPatient()));
                                break;
                            }
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        PopupMenu menu = new PopupMenu(getActivity(), view);
        MenuInflater inflater = menu.getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu.getMenu());

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = null;

                switch (item.getItemId()){
                    case R.id.menu_update_home:
                        updateDialog.startDialog();
                        break;

                    case R.id.menu_whats_new_home:
                        whatsNewDialog.startDialog();
                        break;

                    case R.id.menu_feedback_home:
                        intent = new Intent(getActivity(), FeedbackActivity.class);
                        break;

                    case R.id.menu_about_home:
                        intent = new Intent(getActivity(), AboutActivity.class);
                        break;
                }

                if (intent != null) startActivity(intent);
                return false;
            }
        });

        menu.show();
    }
}
