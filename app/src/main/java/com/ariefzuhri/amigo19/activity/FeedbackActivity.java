package com.ariefzuhri.amigo19.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ariefzuhri.amigo19.customview.LoadingDialog;
import com.ariefzuhri.amigo19.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.ariefzuhri.amigo19.utils.AppUtils.getTransparentNotifBar;

public class FeedbackActivity extends AppCompatActivity {
    private static final String URL = "https://docs.google.com/forms/d/e/1FAIpQLSdVfULnWDMSLKaVRiFom3WF1N07QZWmIefBWjwuhTKl14S_5A/viewform";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        getTransparentNotifBar(this);

        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.startDialog();

        WebView webView = findViewById(R.id.web_view_feedback);
        webView.getSettings().setJavaScriptEnabled(true);

        // Mencegah webview membuka browser
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // Hentikan loading jika laman berhasil dimuat
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                loadingDialog.dismissDialog();
            }
        });

        webView.loadUrl(URL);

        FloatingActionButton btnBack = findViewById(R.id.btn_back_feedback);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
