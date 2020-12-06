package com.ariefzuhri.amigo19.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.TextView;

import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.adapter.MeetAdapter;
import com.ariefzuhri.amigo19.database.MappingHelper;
import com.ariefzuhri.amigo19.interfaces.LoadAssessmentPreferenceCallback;
import com.ariefzuhri.amigo19.interfaces.LoadMeetsCallback;
import com.ariefzuhri.amigo19.model.Meet;
import com.ariefzuhri.amigo19.preference.AssessmentFirestore;
import com.ariefzuhri.amigo19.preference.model.AssessmentPreference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.ariefzuhri.amigo19.activity.MeetAddUpdateActivity.REQUEST_ADD;
import static com.ariefzuhri.amigo19.activity.MeetAddUpdateActivity.REQUEST_UPDATE;
import static com.ariefzuhri.amigo19.activity.MeetAddUpdateActivity.RESULT_ADD;
import static com.ariefzuhri.amigo19.activity.MeetAddUpdateActivity.RESULT_DELETE;
import static com.ariefzuhri.amigo19.activity.MeetAddUpdateActivity.RESULT_UPDATE;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.CONTENT_URI_MEET;
import static com.ariefzuhri.amigo19.utils.AppUtils.getTransparentNotifBar;
import static com.ariefzuhri.amigo19.utils.AppUtils.showSnackbar;
import static com.ariefzuhri.amigo19.utils.AssessmentUtils.getNextMeet;
import static com.ariefzuhri.amigo19.utils.DateUtils.getCurrentDate;

public class MeetDetailActivity extends AppCompatActivity implements LoadMeetsCallback {
    private static final String EXTRA_STATE = "extra_state";

    private TextView tvTitle;
    private MeetAdapter adapter;
    private AssessmentFirestore assessmentFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_detail);

        // Atur notif bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        getTransparentNotifBar(this);

        // Atur recycler view
        RecyclerView rvMeet = findViewById(R.id.rv_meet_detail);
        rvMeet.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MeetAdapter(this);
        adapter.notifyDataSetChanged();
        rvMeet.setAdapter(adapter);

        // Agar ketika CRUD, tampilan diupdate realtime
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver dataObserver = new DataObserver(handler, this, this);
        getContentResolver().registerContentObserver(CONTENT_URI_MEET, true, dataObserver);
        if (savedInstanceState == null){
            new LoadMeetAsync(this, this).execute();
        } else {
            ArrayList<Meet> listMeets = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            adapter.setData(listMeets);
        }

        // Atur title
        tvTitle = findViewById(R.id.tv_title_meet_detail);
        assessmentFirestore = new AssessmentFirestore();
        assessmentFirestore.fetchPreference(new LoadAssessmentPreferenceCallback() {
            @Override
            public void onCallback(AssessmentPreference assessmentPreference) {
                String nextMeet = getNextMeet(assessmentFirestore, assessmentPreference);
                if (nextMeet.equals(getCurrentDate())) tvTitle.setText(getString(R.string.meet_home, getString(R.string.today).toLowerCase()));
                else tvTitle.setText(getString(R.string.meet_home, getString(R.string.future).toLowerCase()));
            }
        });

        // Tombol kembali
        FloatingActionButton btnBack = findViewById(R.id.btn_back_meet_detail);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        Kita sebenarnya sudah tidak membutuhkan onActivityResult untuk memperbarui RecyclerView secara realtime
        Karena fungsi tersebut sudah digantikan oleh HandlerThread

        Di sini kita menggunakan onActivityResult hanya untuk menampilkan pemberitahuan sebagai respon dari aksi yang dilakukan oleh user
         */
        if (data != null){ // Pesan tidak mau muncul
            View view = findViewById(R.id.activity_meet_detail);
            if (requestCode == REQUEST_ADD){
                if (resultCode == RESULT_ADD) showSnackbar(view, getString(R.string.snackbar_add_meet));
            } else if (requestCode == REQUEST_UPDATE){
                if (resultCode == RESULT_UPDATE) showSnackbar(view, getString(R.string.snackbar_update_meet));
                else if (resultCode == RESULT_DELETE) showSnackbar(view, getString(R.string.snackbar_delete_meet));
            }
        }
    }

    private static class LoadMeetAsync extends AsyncTask<Void, Void, ArrayList<Meet>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMeetsCallback> weakCallback;

        private LoadMeetAsync(Context context, LoadMeetsCallback callback){
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Meet> doInBackground(Void... voids) {
            ArrayList<Meet> meets = new ArrayList<>();
            Cursor cursor = weakContext.get().getContentResolver().query(CONTENT_URI_MEET, null, null, null, null);

            if (cursor != null){
                meets.addAll(MappingHelper.mapCursorToArrayList(cursor));
                cursor.close();
            }

            return meets;
        }

        @Override
        protected void onPostExecute(ArrayList<Meet> meets){
            super.onPostExecute(meets);
            weakCallback.get().postExecute(meets);
        }
    }

    @Override
    public void preExecute() {}

    @Override
    public void postExecute(ArrayList<Meet> meets) {
        adapter.setData(meets);
    }


    private static class DataObserver extends ContentObserver {
        final Context context;
        final LoadMeetsCallback callback;

        DataObserver(Handler handler, Context context, LoadMeetsCallback callback) {
            super(handler);
            this.context = context;
            this.callback = callback;
        }

        @Override
        public void onChange(boolean selfChange){
            super.onChange(selfChange);
            new LoadMeetAsync(context, callback).execute();
        }
    }
}