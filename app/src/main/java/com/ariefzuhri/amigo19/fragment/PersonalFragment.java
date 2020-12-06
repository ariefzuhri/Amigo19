package com.ariefzuhri.amigo19.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.activity.SettingLocationActivity;
import com.ariefzuhri.amigo19.activity.SplashActivity;
import com.ariefzuhri.amigo19.adapter.SuggestionAdapter;
import com.ariefzuhri.amigo19.interfaces.LoadAssessmentPreferenceCallback;
import com.ariefzuhri.amigo19.interfaces.LoadUserPreferenceCallback;
import com.ariefzuhri.amigo19.preference.AssessmentFirestore;
import com.ariefzuhri.amigo19.preference.model.AssessmentPreference;
import com.ariefzuhri.amigo19.preference.UserFirestore;
import com.ariefzuhri.amigo19.preference.model.UserPreference;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;
import static com.ariefzuhri.amigo19.utils.DateUtils.getFullDate;

public class PersonalFragment extends Fragment implements View.OnClickListener {
    private FirebaseUser user;
    private TextView tvName, tvEmail, tvLocation, tvDateAssessment, tvResult1Assessment, tvResult2Assessment, tvSuggestion;
    private AssessmentFirestore assessmentFirestore;
    private SuggestionAdapter adapter;
    private UserFirestore userFirestore;
    private Context context;

    public PersonalFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // Initialize
        assessmentFirestore = new AssessmentFirestore();
        userFirestore = new UserFirestore();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        context = getActivity();

        TextView tvSignOut = view.findViewById(R.id.tv_sign_out_personal);
        TextView tvReassignment = view.findViewById(R.id.tv_reassessment_personal);
        ImageButton btnSetupLocation = view.findViewById(R.id.btn_setup_location_personal);
        tvName = view.findViewById(R.id.tv_name_personal);
        tvEmail = view.findViewById(R.id.tv_email_personal);
        tvLocation = view.findViewById(R.id.tv_location_personal);
        tvDateAssessment = view.findViewById(R.id.tv_date_assessment_personal);
        tvResult1Assessment = view.findViewById(R.id.tv_result_1_assessment_personal);
        tvResult2Assessment = view.findViewById(R.id.tv_result_2_assessment_personal);
        tvSuggestion = view.findViewById(R.id.tv_suggestion_personal);
        RecyclerView rvSuggestion = view.findViewById(R.id.rv_suggestion_personal);

        // Recycler view
        rvSuggestion.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SuggestionAdapter(getActivity());
        adapter.notifyDataSetChanged();
        rvSuggestion.setAdapter(adapter);

        loadView();

        // Atur listener
        tvSignOut.setOnClickListener(this);
        tvReassignment.setOnClickListener(this);
        btnSetupLocation.setOnClickListener(this);
    }

    private void loadView(){
        tvName.setText(user.getDisplayName());
        tvEmail.setText(user.getEmail());

        // Muat personal sug ke adapter
        assessmentFirestore.fetchPreference(new LoadAssessmentPreferenceCallback() {
            @Override
            public void onCallback(AssessmentPreference assessmentPreference) {
                List<Integer> indexSuggestion = assessmentPreference.getIndexSuggestion();
                adapter.setData(indexSuggestion);

                // Muat text2-an
                String[] levelMeet = getActivity().getResources().getStringArray(R.array.level_meet);
                tvDateAssessment.setText(getFullDate(assessmentPreference.getDateAssessment()));
                tvResult1Assessment.setText(getActivity().getString(R.string.result_1_assessment, assessmentPreference.getRangeMeet()));
                tvResult2Assessment.setText(getActivity().getString(R.string.result_2_assessment, assessmentPreference.getRangeMeet()));
                tvSuggestion.setText(getActivity().getString(R.string.result_3_assessment, levelMeet[assessmentPreference.getLevelMeet()]));
            }
        });

        userFirestore.fetchPreference(new LoadUserPreferenceCallback() {
            @Override
            public void onCallback(UserPreference userPreference) {
                tvLocation.setText(userPreference.getLocation());
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        switch (view.getId()){
            case R.id.tv_sign_out_personal:
                dialog.setMessage(R.string.dialog_sign_out_personal);
                dialog.setPositiveButton(R.string.dialog_cancel, null);
                dialog.setNegativeButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(getString(R.string.default_web_client_id))
                                        .requestEmail()
                                        .build();
                                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

                                // Google and Firebase sign out
                                googleSignInClient.signOut(); // Ini juga, agar nanti saat login bisa pilih akun Google yang lain
                                FirebaseAuth.getInstance().signOut();

                                // Akses aplikasi wajib login
                                restartApplication();
                            }
                        });
                dialog.create().show();
                break;

            case R.id.tv_reassessment_personal:
                dialog.setMessage(R.string.dialog_reassessment_personal);
                dialog.setNegativeButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                assessmentFirestore.resetPreference();
                                restartApplication();
                            }
                        });
                dialog.setPositiveButton(R.string.dialog_cancel, null);
                dialog.create().show();
                break;

            case R.id.btn_setup_location_personal:
                intent = new Intent(getActivity(), SettingLocationActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void restartApplication() {
        Intent intent = new Intent(getActivity(), SplashActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
