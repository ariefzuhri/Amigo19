package com.ariefzuhri.amigo19.preference;

import android.util.Log;

import com.ariefzuhri.amigo19.BuildConfig;
import com.ariefzuhri.amigo19.interfaces.LoadUpdateDataCallback;
import com.ariefzuhri.amigo19.preference.model.UpdateInformation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static com.ariefzuhri.amigo19.utils.AppUtils.LOG;
import static com.ariefzuhri.amigo19.utils.DateUtils.getCurrentDate;

public class UpdateFirestore {
    private static final String TAG = "UpdateFirestore";

    private DocumentReference documentReference;
    private UpdateInformation updateInformation;

    public UpdateFirestore(){
        documentReference = FirebaseFirestore.getInstance().document("appInformation/update");
        updateInformation = new UpdateInformation();
    }

    public void fetchPreference(final LoadUpdateDataCallback callback){
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) updateInformation = documentSnapshot.toObject(UpdateInformation.class);
                else updateInformation = new UpdateInformation(getCurrentDate(), BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, null, new ArrayList<String>());
                callback.onCallback(updateInformation);
                Log.d(TAG, LOG + "latest version:" + updateInformation.getLatestVersion() + " code:" + updateInformation.getLatestVersionCode());
            }
        });
    }
}
