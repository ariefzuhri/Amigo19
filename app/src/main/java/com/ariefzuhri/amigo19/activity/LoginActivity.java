package com.ariefzuhri.amigo19.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.ariefzuhri.amigo19.customview.LoadingDialog;
import com.ariefzuhri.amigo19.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ornach.nobobutton.NoboButton;

import static com.ariefzuhri.amigo19.utils.AppUtils.LOG;
import static com.ariefzuhri.amigo19.utils.AppUtils.getTransparentNotifBar;
import static com.ariefzuhri.amigo19.utils.AppUtils.showSnackbar;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Atur status bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        getTransparentNotifBar(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize
        loadingDialog = new LoadingDialog(this);
        NoboButton btnLogin = findViewById(R.id.btn_google_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly
        // Untuk pengguna yang reasesmen, akan tetap melewati login, namun langsung skip (onStart pada login) ke setup karena sudah login
        // Jika belum login, baik pengguna lama maupun baru, baik sudah asesmen atau blm, tidak bisa skip
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) launchAssessmentOrMain();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e){
                // Google Sign In failed or user press back button
                Log.w(TAG, "Google sign in failed", e);
                showSnackbar(findViewById(R.id.activity_login), getString(R.string.snackbar_fail_login));
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, LOG + "firebaseAuthWithGoogle:" + account.getId());
        loadingDialog.startDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // Sign in success, update UI with the signed-in user's informationZ
                            Log.d(TAG, LOG + "signInWithCredential:success");
                            launchAssessmentOrMain();
                        } else {
                            // If sign in fails, display a message to the user
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            showSnackbar(findViewById(R.id.activity_login), getString(R.string.snackbar_fail_auth_login));
                        }

                        loadingDialog.dismissDialog();
                    }
                });
    }

    private void signIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void launchAssessmentOrMain(){
        /*assessmentFirestore.fetchPreference(new LoadAssessmentPreferenceCallback() {
            @Override
            public void onCallback(AssessmentPreference assessmentPreference) {
                Intent intent;
                if (assessmentPreference.getIsFillOut()) intent = new Intent(LoginActivity.this, MainActivity.class);
                else intent = new Intent(LoginActivity.this, PreAssessmentActivity.class);
                startActivity(intent);
                finish();
            }
        });*/
        // Restart aplikasi
        Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
        startActivity(intent);
        finish();
    }
}
