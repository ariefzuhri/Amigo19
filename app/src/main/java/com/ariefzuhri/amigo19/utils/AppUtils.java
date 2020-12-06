package com.ariefzuhri.amigo19.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.core.app.NotificationCompat;

import com.ariefzuhri.amigo19.BuildConfig;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.interfaces.LoadUpdateDataCallback;
import com.ariefzuhri.amigo19.preference.model.UpdateInformation;
import com.ariefzuhri.amigo19.preference.UpdateFirestore;
import com.blongho.country_data.World;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.NumberFormat;
import java.util.Locale;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;
import static com.ariefzuhri.amigo19.utils.DateUtils.getCurrentTime;

public class AppUtils {
    public static final String LOG = "App log -> ";
    private static final String TAG = "AppUtils";
    static Locale locale = new Locale("in", "ID");

    // Membuat notification bar menjadi transparan
    public static void getTransparentNotifBar(Activity activity){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    // Mendapatkan ucapan salam
    public static String getGreetings(Context context, String name){
        String greetings;
        String time = getCurrentTime();

        String[] timeArray = time.split(":");
        int HH = Integer.parseInt(timeArray[0]);
        int mm = Integer.parseInt(timeArray[1]);
        mm += (HH*60);

        if (mm >= 180 && mm <= 600){ // Jam 3-10
            greetings = context.getString(R.string.good_morning);
        } else if (mm > 600 && mm <= 900){ // Jam 10-15
            greetings = context.getString(R.string.good_afternoon);
        } else if (mm > 900 && mm <= 1080){ // Jam 15-18
            greetings = context.getString(R.string.good_evening);
        } else{ // ((mm > 1080) || (mm >= 0 && mm < 180)) // Jam 18-6
            greetings = context.getString(R.string.good_night);
        }

        return greetings + ", " + name;
    }

    // Mendapatkan resource shape
    public static int getTheme(String theme){
        switch (theme){
            case "main": return R.drawable.shape_main;
            case "reverse": return R.drawable.shape_reverse;
            case "b": return R.drawable.shape_b;
            case "c": return R.drawable.shape_c;
            case "d": return R.drawable.shape_d;
            case "e": return R.drawable.shape_e;
            case "f": return R.drawable.shape_f;
            case "g": return R.drawable.shape_g;
            case "h": return R.drawable.shape_h;
            default: return 0;
        }
    }

    // Menampilkan snackbar
    public static void showSnackbar(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setDuration(6000).show();
    }

    // Mendapatkan link lambang provinsi
    public static String getFlagIndonesia(Activity activity, int id){
        return activity.getResources().getStringArray(R.array.provinces_emblem)[id-1];
    }

    // Mendapatkan drawable bendera negara
    public static int getFlagWorld(Activity activity, String countryName){
        // Custom fix bendera
        switch (countryName) {
            case "Korea, South": return R.drawable.kr;
            case "Taiwan*": return R.drawable.tw;
            case "Cote d'Ivoire": return R.drawable.ci;
            case "West Bank and Gaza": return R.drawable.ps;
            case "Congo (Kinshasa)": return R.drawable.cd;
            case "Congo (Brazzaville)": return R.drawable.cg;
            case "Burma": return R.drawable.mm;
            case "Saint Kitts and Nevis": return R.drawable.kn;
            case "Holy See": return R.drawable.va;
            case "Saint Vincent and the Grenadines": return R.drawable.vc;
            case "Kosovo": return R.drawable.ks;
            case "Sao Tome and Principe": return R.drawable.st;
            case "Korea, North": return R.drawable.kp;

            case "Diamond Princess":
            case "MS Zaandam":
                return R.drawable.no_flag;
        }

        World.init(activity);
        return World.getFlagOf(countryName);
    }

    // Memuat gambar menggunakan glide
    public static void loadImage(Activity activity, ImageView imgView, String imgPath){
        Glide.with(activity)
                .load(imgPath)
                .apply(RequestOptions.placeholderOf(R.drawable.no_flag)) // Taro gambar sebelum selesai di-load
                .error(R.drawable.no_flag) // Jika terjadi error
                .into(imgView);
    }

    // Konversi titik setelah tiga angka (xx.xxx)
    public static String getDecimalFormat(int number){
        return NumberFormat.getNumberInstance(locale).format(number);
    }

    // Mengecek apakah string bernilai null
    public static boolean isNull(String text){
        return TextUtils.isEmpty(text);
    }

    // Mendapatkan string tanpa spasi berlebihan
    public static String getFixText(ExtendedEditText editText){
        return (editText.getText().toString().trim()).replaceAll("\\s+", " ");
    }

    // Mengecek apakah karakter yang diinput melebih batas karakter
    public static boolean isMaxChar(ExtendedEditText editText, TextFieldBoxes fieldBoxes){
        return editText.getText().toString().trim().replace(" ", "").length() > fieldBoxes.getMaxCharacters();
    }

    public static String getFirstName(String fullName){
        return fullName.split(" ")[0];
    }

    public static Notification sendNotification(Context context, String channelId, String channelName, Intent intent, String title, String message, boolean isAutoCancel, boolean isOnGoing){
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.logo_notif)
                .setColor(context.getResources().getColor(R.color.cyan))
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .setAutoCancel(isAutoCancel)
                .setOngoing(isAutoCancel);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationBuilder.setChannelId(channelId);
            if (notificationManager != null) notificationManager.createNotificationChannel(channel);
        }

        Notification notification = notificationBuilder.build();
        if (notificationManager != null) notificationManager.notify(1, notification);

        return notification;
    }

    public static void subscribeForUpdate(){
        UpdateFirestore updateFirestore = new UpdateFirestore();
        updateFirestore.fetchPreference(new LoadUpdateDataCallback() {
            @Override
            public void onCallback(UpdateInformation updateInformation) {
                if (BuildConfig.VERSION_CODE < updateInformation.getLatestVersionCode()){
                    /*Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(updateInformation.getLink()));
                    sendNotification(context, context.getString(R.string.notif_channel_id_update), "Update",
                            intent, context.getString(R.string.notif_title_update), context.getString(R.string.notif_desc_update),
                            true, false);*/
                    FirebaseMessaging.getInstance().subscribeToTopic("Update");
                    Log.d(TAG, LOG + "An update is available. Subscribe to topic.");
                }
                else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Update");
                    Log.d(TAG, LOG + "App is up to date. Unsubscribe from topic.");
                }
            }
        });
    }
}
