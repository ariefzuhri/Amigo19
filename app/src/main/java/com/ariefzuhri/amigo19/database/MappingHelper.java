package com.ariefzuhri.amigo19.database;

import android.database.Cursor;
import com.ariefzuhri.amigo19.model.Meet;

import java.util.ArrayList;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.IS_CHECKED;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.NAME;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns.QUANTITY;
import static com.ariefzuhri.amigo19.database.DatabaseContract.MeetColumns._ID;

public class MappingHelper {
    public static ArrayList<Meet> mapCursorToArrayList(Cursor cursor){
        ArrayList<Meet> listMeets = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME));
            double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(QUANTITY));
            boolean isChecked = cursor.getInt(cursor.getColumnIndexOrThrow(IS_CHECKED))==1;
            listMeets.add(new Meet(id, name, quantity, isChecked));
        }
        return listMeets;
    }
}
