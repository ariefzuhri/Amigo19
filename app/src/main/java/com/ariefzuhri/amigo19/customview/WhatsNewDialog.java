package com.ariefzuhri.amigo19.customview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ariefzuhri.amigo19.BuildConfig;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.interfaces.LoadUpdateDataCallback;
import com.ariefzuhri.amigo19.preference.model.UpdateInformation;
import com.ariefzuhri.amigo19.preference.UpdateFirestore;
import com.ornach.nobobutton.NoboButton;

import java.util.ArrayList;

import static com.ariefzuhri.amigo19.utils.DateUtils.getFullDate;

public class WhatsNewDialog {
    private Activity activity;
    private AlertDialog dialog;
    private LoadingDialog loadingDialog;
    private UpdateFirestore updateFirestore;

    private TextView tvVersion, tvLog;

    @SuppressLint("InflateParams")
    public WhatsNewDialog(Activity context){
        this.activity = context;

        // Initialize
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_whats_new, null);
        loadingDialog = new LoadingDialog(context);
        updateFirestore = new UpdateFirestore();

        tvVersion = view.findViewById(R.id.tv_version_whats_new);
        tvLog = view.findViewById(R.id.tv_log_whats_new);
        NoboButton btnClose = view.findViewById(R.id.btn_close_whats_new);

        // Create alert dialog instance just once
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setCancelable(true);
        dialog = builder.create();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void startDialog(){
        // Cek pembaruan
        loadingDialog.startDialog();
        updateFirestore.fetchPreference(new LoadUpdateDataCallback() {
            @Override
            public void onCallback(UpdateInformation updateInformation) {
                String date = updateInformation.getDate();
                ArrayList<String> log = updateInformation.getLog();

                tvVersion.setText(activity.getString(R.string.version_whats_new, BuildConfig.VERSION_NAME, getFullDate(date)));
                tvLog.setText(TextUtils.join("\n\n", log));

                loadingDialog.dismissDialog();
                dialog.show();
            }
        });
    }
}
