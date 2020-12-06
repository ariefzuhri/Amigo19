package com.ariefzuhri.amigo19.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns._ID;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.TABLE_MEET;

// Kelas ini disebut DML (Data Manipulation Language)
// Perintah SQL yang berhubungan dengan pengolah data dalam tabel
public class MeetHelper {
    private static final String DATABASE_TABLE = TABLE_MEET;
    private static DatabaseHelper databaseHelper;
    private static MeetHelper INSTANCE;
    private static SQLiteDatabase database;

    private MeetHelper(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    public static MeetHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null) INSTANCE = new MeetHelper(context);
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException{
        database = databaseHelper.getWritableDatabase();
    }

    public void close(){
        databaseHelper.close();
        if (database.isOpen()) database.close();
    }

    public Cursor queryAll(){
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC"
        );
    }

    public Cursor query(String id){
        return database.query(
                DATABASE_TABLE,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null
        );
    }

    public long insert(ContentValues contentValues){
        return database.insert(
                DATABASE_TABLE,
                null,
                contentValues
        );
    }

    public int update(String id, ContentValues contentValues){
        return database.update(
                DATABASE_TABLE,
                contentValues,
                _ID + " = ?",
                new String[]{id}
        );
    }

    public int delete(String id){
        return database.delete(
                DATABASE_TABLE,
                _ID + " = ?",
                new String[]{id}
        );
    }
}
