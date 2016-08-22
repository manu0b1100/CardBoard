package com.example.manobhavjain.projectkasm.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.manobhavjain.projectkasm.Database.DbSchema.CardsTable;

/**
 * Created by Manobhav Jain on 8/11/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final  int VERSION=1;
    private static final String DATABASE_NAME="database.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+ CardsTable.NAME+"("+
        "_id integer primary key autoincrement, "+
                CardsTable.Cols.UUID+", "+
                CardsTable.Cols.JSONSTRING+ ")"
        );




    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
