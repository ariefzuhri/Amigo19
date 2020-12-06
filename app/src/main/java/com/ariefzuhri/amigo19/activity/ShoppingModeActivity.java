package com.ariefzuhri.amigo19.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.adapter.MeetAdapter;
import com.ariefzuhri.amigo19.database.MappingHelper;
import com.ariefzuhri.amigo19.interfaces.LoadMeetsCallback;
import com.ariefzuhri.amigo19.localpreference.ShoppingModePreference;
import com.ariefzuhri.amigo19.model.Meet;
import com.ariefzuhri.amigo19.service.ShoppingModeService;
import com.ornach.nobobutton.NoboButton;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.CONTENT_URI_MEET;
import static com.ariefzuhri.amigo19.utils.AppUtils.getTransparentNotifBar;

public class ShoppingModeActivity extends AppCompatActivity implements View.OnClickListener, LoadMeetsCallback, CheckBox.OnCheckedChangeListener {
    private static final String EXTRA_STATE = "extra_state";

    private int stepCount = 1;
    private String[] steps, status;

    private TextView tvStatus;
    private CheckBox cb1, cb2, cb3, cb4, cb5;
    private NoboButton btnNext;
    private MeetAdapter adapter;

    private ShoppingModePreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_mode);

        // Atur status bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        getTransparentNotifBar(this);

        // Initialize
        preference = new ShoppingModePreference(this);
        preference.setIsShoppingMode(true);

        Intent intent = new Intent(this, ShoppingModeService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(intent);
        startService(intent);

        RecyclerView rvMeet = findViewById(R.id.rv_meet_shopping_mode);
        tvStatus = findViewById(R.id.tv_status_shopping_mode);
        btnNext = findViewById(R.id.btn_next_shopping_mode);
        NoboButton btnExit = findViewById(R.id.btn_exit_shopping_mode);
        cb1 = findViewById(R.id.cb_1_shopping_mode);
        cb2 = findViewById(R.id.cb_2_shopping_mode);
        cb3 = findViewById(R.id.cb_3_shopping_mode);
        cb4 = findViewById(R.id.cb_4_shopping_mode);
        cb5 = findViewById(R.id.cb_5_shopping_mode);

        status = getResources().getStringArray(R.array.status_shopping_mode);

        // Atur daftar belanja
        rvMeet.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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

        // Atur listener
        btnNext.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        cb1.setOnCheckedChangeListener(this);
        cb2.setOnCheckedChangeListener(this);
        cb3.setOnCheckedChangeListener(this);
        cb4.setOnCheckedChangeListener(this);
        cb5.setOnCheckedChangeListener(this);

        loadPreference();
        loadChecklist();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getData());
        savePreference();
    }

    private void loadChecklist() {
        if (stepCount > 3){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setCancelable(false);
            dialog.setTitle(R.string.yeay);
            dialog.setMessage(R.string.dialog_finish_shopping_mode);
            dialog.setNeutralButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switchOffMode();
                        }
                    });
            dialog.create().show();
        }
        else {
            setBtnNextEnabled();
            tvStatus.setText(getString(R.string.status_shopping_mode, status[stepCount-1]));

            if (stepCount == 1){
                btnNext.setText(getString(R.string.finish_1_shopping_mode));
                steps = getResources().getStringArray(R.array.first_step_shopping_mode);
            }
            else if (stepCount == 2){
                btnNext.setText(getString(R.string.finish_2_shopping_mode));
                steps = getResources().getStringArray(R.array.second_step_shopping_mode);
            }
            else if (stepCount == 3){
                btnNext.setText(getString(R.string.finish_3_shopping_mode));
                steps = getResources().getStringArray(R.array.third_step_shopping_mode);
            }

            cb1.setText(steps[0]);
            cb2.setText(steps[1]);
            cb3.setText(steps[2]);
            cb4.setText(steps[3]);
            cb5.setText(steps[4]);
        }
    }

    @Override
    public void preExecute() {}

    @Override
    public void postExecute(ArrayList<Meet> meets) {
        adapter.setData(meets);
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        setBtnNextEnabled();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_next_shopping_mode:
                stepCount++;
                loadChecklist();
                /*Jangan taruh di loadChecklist()
                * Karena loadChecklist selalu dimuat ketika buka aplikasi
                * Akibatnya setelah atur cb mana saja yang checked sesuai preference,
                * Ujung-ujungnya diatur kembali semua cb menjadi unchecked
                * Jika ditaruh di sini, cb diatur ulang hanya setelah btnNext diketuk*/
                cb1.setChecked(false);
                cb2.setChecked(false);
                cb3.setChecked(false);
                cb4.setChecked(false);
                cb5.setChecked(false);
                break;

            case R.id.btn_exit_shopping_mode:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage(R.string.dialog_exit_shopping_mode);
                dialog.setPositiveButton(R.string.dialog_cancel, null);
                dialog.setNegativeButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switchOffMode();
                            }
                        });
                dialog.create().show();
                break;
        }
    }

    @Override
    public void onStop() {
        if (preference.isShoppingMode()) savePreference();
        else resetPreference(); // alternatif -> delete preference file
        super.onStop();
    }

    private void loadPreference() {
        stepCount = preference.getStepCount();
        cb1.setChecked(preference.isCb1Checked());
        cb2.setChecked(preference.isCb2Checked());
        cb3.setChecked(preference.isCb3Checked());
        cb4.setChecked(preference.isCb4Checked());
        cb5.setChecked(preference.isCb5Checked());
    }

    private void savePreference() {
        preference.setStepCountShoppingMode(stepCount);
        preference.setIsCb1Checked(cb1.isChecked());
        preference.setIsCb2Checked(cb2.isChecked());
        preference.setIsCb3Checked(cb3.isChecked());
        preference.setIsCb4Checked(cb4.isChecked());
        preference.setIsCb5Checked(cb5.isChecked());
    }

    private void resetPreference() {
        preference.setStepCountShoppingMode(1);
        preference.setIsCb1Checked(false);
        preference.setIsCb2Checked(false);
        preference.setIsCb3Checked(false);
        preference.setIsCb4Checked(false);
        preference.setIsCb5Checked(false);
    }

    private void switchOffMode(){
        preference.setIsShoppingMode(false);
        stopService(new Intent(ShoppingModeActivity.this, ShoppingModeService.class));
        Intent intent = new Intent(ShoppingModeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setBtnNextEnabled() {
        if (cb1.isChecked() && cb2.isChecked() && cb3.isChecked() &&
                cb4.isChecked() && cb5.isChecked()) btnNext.setEnabled(true);
        else btnNext.setEnabled(false);
    }
}