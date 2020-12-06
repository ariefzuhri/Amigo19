package com.ariefzuhri.amigo19.preference;

import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;

import com.ariefzuhri.amigo19.BuildConfig;
import com.ariefzuhri.amigo19.interfaces.LoadUserPreferenceCallback;
import com.ariefzuhri.amigo19.preference.model.UserPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

import static com.ariefzuhri.amigo19.utils.AppUtils.LOG;

public class UserFirestore {
    private static final String TAG = "UserFirestore";

    private DocumentReference documentReference;
    private UserPreference userPreference;

    private static final String IS_CUSTOM_LOCATION_KEY = "isCustomLocation";
    private static final String LOCATION_KEY = "location";
    private static final String IS_ABROAD_KEY = "isAbroad";
    private static final String USER_VERSION = "userVersion";
    private static final String ANDROID_VERSION = "androidVersion";

    public UserFirestore(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        documentReference = FirebaseFirestore.getInstance().document("userPreference/" + firebaseUser.getUid());
        userPreference = new UserPreference();
    }

    public void savePreference(boolean isCustomLocation, String location, boolean isAbroad){
        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put(IS_CUSTOM_LOCATION_KEY, isCustomLocation);
        dataToSave.put(LOCATION_KEY, location);
        dataToSave.put(IS_ABROAD_KEY, isAbroad);

        dataToSave.put(USER_VERSION, BuildConfig.VERSION_CODE);
        dataToSave.put(ANDROID_VERSION, Build.VERSION.SDK_INT);

        documentReference.set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) Log.d(TAG, LOG + "Document was saved.");
                else Log.w(TAG, "failure", task.getException());
            }
        });
    }

    public void fetchPreference(final LoadUserPreferenceCallback callback){
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) userPreference = documentSnapshot.toObject(UserPreference.class);
                else userPreference = new UserPreference(true, "-", false);
                callback.onCallback(userPreference);
            }
        });
    }
}
