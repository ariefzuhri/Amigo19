package com.ariefzuhri.amigo19.customview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ariefzuhri.amigo19.BuildConfig;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.interfaces.LoadUpdateDataCallback;
import com.ariefzuhri.amigo19.preference.model.UpdateInformation;
import com.ariefzuhri.amigo19.preference.UpdateFirestore;
import com.ornach.nobobutton.NoboButton;

public class UpdateDialog {
    private Activity activity;
    private AlertDialog dialog;
    private LoadingDialog loadingDialog;
    private UpdateFirestore updateFirestore;

    private TextView tvTitle, tvDesc;
    private NoboButton btnUpdate;

    private boolean isUpdateAvailable;
    private String latestVersion;
    private String url;

    @SuppressLint("InflateParams")
    public UpdateDialog(Activity context){
        this.activity = context;

        // Initialize
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_update, null);
        loadingDialog = new LoadingDialog(activity);
        updateFirestore = new UpdateFirestore();

        tvTitle = view.findViewById(R.id.tv_title_update);
        tvDesc = view.findViewById(R.id.tv_desc_update);
        btnUpdate = view.findViewById(R.id.btn_update);

        // Create alert dialog instance just once
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setCancelable(false);
        dialog = builder.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnUpdate.getText().equals(activity.getString(R.string.download))){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    activity.startActivity(intent);
                }
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
                isUpdateAvailable = BuildConfig.VERSION_CODE < updateInformation.getLatestVersionCode();
                latestVersion = updateInformation.getLatestVersion();
                url = updateInformation.getLink();

                if (isUpdateAvailable){
                    tvTitle.setText(activity.getString(R.string.title_update, latestVersion));
                    tvDesc.setText(R.string.desc_tutorial_update);
                    btnUpdate.setText(activity.getString(R.string.download));
                } else {
                    tvTitle.setText(activity.getString(R.string.yeay));
                    tvDesc.setText(R.string.desc_uptodate_update);
                    btnUpdate.setText(activity.getString(R.string.close));
                }

                loadingDialog.dismissDialog();
                dialog.show();
            }
        });
    }
}
