package com.ariefzuhri.amigo19.service;

import android.content.Intent;

import android.util.Log;

import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import static com.ariefzuhri.amigo19.utils.AppUtils.LOG;
import static com.ariefzuhri.amigo19.utils.AppUtils.sendNotification;

public class MessagingService extends FirebaseMessagingService {
    private static final String TAG = "MessagingService";

    @Override
    // Mengambil token dari firebase
    public void onNewToken(@NotNull String s){
        super.onNewToken(s);
        Log.d(TAG, LOG + "Refreshed token:" + s);
    }

    @Override
    // Menerima notif
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null){
            sendNotification(this, getString(R.string.notif_channel_id_fcm), "News",
                    new Intent(this, MainActivity.class), null, remoteMessage.getNotification().getBody(),
                    true, false);
        }
    }
}
