package com.ariefzuhri.amigo19.preference;

import android.util.Log;
import androidx.annotation.NonNull;
import com.ariefzuhri.amigo19.interfaces.LoadAssessmentPreferenceCallback;
import com.ariefzuhri.amigo19.preference.model.AssessmentPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ariefzuhri.amigo19.utils.AppUtils.LOG;

public class AssessmentFirestore {
    private static final String TAG = "AssessmentFirestore";

    private DocumentReference documentReference;
    private AssessmentPreference assessmentPreference;

    private static final String IS_FILL_OUT_KEY = "isFillOut"; // apakah sudah isi asesmen
    private static final String DATE_ASSESSMENT_KEY = "dateAssessment"; // tanggal isi asesmen // TYPO
    private static final String FIRST_MEET_KEY = "firstMeet"; // tanggal belanja pertama
    private static final String LAST_MEET_KEY = "lastMeet"; // tanggal belanja terakhir kali
    private static final String POINT_ASSESSMENT_KEY = "pointAssessment"; // poin hasil asesmen
    private static final String INDEX_SUGGESTION_KEY = "indexSuggestion";
    private static final String NUMBER_OF_DELAY_KEY = "numberOfDelay";
    private static final String RANGE_MEET_KEY = "rangeMeet";
    private static final String LEVEL_MEET_KEY = "levelMeet";

    public AssessmentFirestore(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        documentReference = FirebaseFirestore.getInstance().document("assessmentPreference/" + firebaseUser.getUid());
        assessmentPreference = new AssessmentPreference();
    }

    public void resetPreference(){
        savePreference(false, null, null, "-", 0, null, 0, 0, 0);
    }

    public void savePreference(boolean isFillOut, String dateAssessment, String firstMeet, String lastMeet, int pointAssessment, List<Integer> indexSuggestion, int numberOfDelay, int rangeMeet, int levelMeet){
        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put(IS_FILL_OUT_KEY, isFillOut);
        dataToSave.put(DATE_ASSESSMENT_KEY, dateAssessment);
        dataToSave.put(FIRST_MEET_KEY, firstMeet);
        dataToSave.put(LAST_MEET_KEY, lastMeet);
        dataToSave.put(POINT_ASSESSMENT_KEY, pointAssessment);
        dataToSave.put(INDEX_SUGGESTION_KEY, indexSuggestion);
        dataToSave.put(NUMBER_OF_DELAY_KEY, numberOfDelay);
        dataToSave.put(RANGE_MEET_KEY, rangeMeet);
        dataToSave.put(LEVEL_MEET_KEY, levelMeet);

        documentReference.set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) Log.d(TAG, LOG + "Document was saved.");
                else Log.w(TAG, "failure", task.getException());
            }
        });
    }

    public void savePreference(String lastMeet){
        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put(IS_FILL_OUT_KEY, assessmentPreference.getIsFillOut());
        dataToSave.put(DATE_ASSESSMENT_KEY, assessmentPreference.getDateAssessment());
        dataToSave.put(FIRST_MEET_KEY, assessmentPreference.getFirstMeet());
        dataToSave.put(LAST_MEET_KEY, lastMeet);
        dataToSave.put(POINT_ASSESSMENT_KEY, assessmentPreference.getPointAssessment());
        dataToSave.put(INDEX_SUGGESTION_KEY, assessmentPreference.getIndexSuggestion());
        dataToSave.put(NUMBER_OF_DELAY_KEY, 0);
        dataToSave.put(RANGE_MEET_KEY, assessmentPreference.getRangeMeet());
        dataToSave.put(LEVEL_MEET_KEY, assessmentPreference.getLevelMeet());

        documentReference.set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) Log.d(TAG, LOG + "Document was saved (lastMeet).");
                else Log.w(TAG, "failure", task.getException());
            }
        });
    }

    public void savePreference(int numberOfDelay){
        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put(IS_FILL_OUT_KEY, assessmentPreference.getIsFillOut());
        dataToSave.put(DATE_ASSESSMENT_KEY, assessmentPreference.getDateAssessment());
        dataToSave.put(FIRST_MEET_KEY, assessmentPreference.getFirstMeet());
        dataToSave.put(LAST_MEET_KEY, assessmentPreference.getLastMeet());
        dataToSave.put(POINT_ASSESSMENT_KEY, assessmentPreference.getPointAssessment());
        dataToSave.put(INDEX_SUGGESTION_KEY, assessmentPreference.getIndexSuggestion());
        dataToSave.put(NUMBER_OF_DELAY_KEY, numberOfDelay);
        dataToSave.put(RANGE_MEET_KEY, assessmentPreference.getRangeMeet());
        dataToSave.put(LEVEL_MEET_KEY, assessmentPreference.getLevelMeet());

        documentReference.set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) Log.d(TAG, LOG + "Document was saved (numberOfDelay).");
                else Log.w(TAG, "failure", task.getException());
            }
        });
    }

    public void fetchPreference(final LoadAssessmentPreferenceCallback callback){
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) assessmentPreference = documentSnapshot.toObject(AssessmentPreference.class);
                else assessmentPreference = new AssessmentPreference(false, null, null, "-", 0, null, 0, 0, 0);
                callback.onCallback(assessmentPreference);
            }
        });
    }
}
