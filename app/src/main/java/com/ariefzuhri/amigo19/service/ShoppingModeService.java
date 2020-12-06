package com.ariefzuhri.amigo19.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.activity.ShoppingModeActivity;

import javax.annotation.Nullable;

import static com.ariefzuhri.amigo19.utils.AppUtils.sendNotification;

public class ShoppingModeService extends Service {
    public ShoppingModeService() {}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Notification notification = sendNotification(
                this, getString(R.string.notif_channel_id_shopping_mode), "Shopping Mode",
                new Intent(this, ShoppingModeActivity.class),
                getString(R.string.notif_title_shopping_mode), getString(R.string.notif_desc_shopping_mode),
                false, true);
        super.startForeground(1, notification);
        return super.onStartCommand(intent, flags, startId);
    }
}
