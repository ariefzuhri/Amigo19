package com.ariefzuhri.amigo19.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.IS_CHECKED;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.NAME;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.QUANTITY;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.TABLE_MEET;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns._ID;

// Kelas ini disebut DDL (Data Definition Language)
// Perintah SQL yang berhubungan dengan pendefinisian suatu struktur database
public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "dbamigo19";
    private static final int DATABASE_VERSION = 1;

    private static final  String SQL_CREATE_TABLE_MEET = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s INTEGER NOT NULL," +
                    " %s INTEGER NOT NULL)",
            TABLE_MEET,
            _ID,
            NAME,
            QUANTITY,
            IS_CHECKED
    );

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MEET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEET);
        onCreate(db);
    }
}
