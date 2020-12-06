package com.ariefzuhri.amigo19.database;

import android.net.Uri;
import android.provider.BaseColumns;

// Mempermudah akses nama tabel dan kolom di db
public class DatabaseContract {
    static final String AUTHORITY = "com.ariefzuhri.amigo19";

    // Kolom _ID sudah ada secara otomatis di kelas BaseColumns
    public static final class MeetColumns implements BaseColumns{
        public static String TABLE_MEET = "meet";
        public static String NAME = "name";
        public static String QUANTITY = "quantity";
        public static String IS_CHECKED = "is_checked";

        public static final Uri CONTENT_URI_MEET = new Uri.Builder()
                .scheme("content")
                .authority(AUTHORITY)
                .appendPath(TABLE_MEET)
                .build();
    }
}
