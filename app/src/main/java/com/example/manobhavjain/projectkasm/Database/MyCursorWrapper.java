package com.example.manobhavjain.projectkasm.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.manobhavjain.projectkasm.Cardbase;
import com.example.manobhavjain.projectkasm.Database.DbSchema.CardsTable;

import java.util.Date;
import java.util.UUID;


/**
 * Created by Manobhav Jain on 8/11/2016.
 */
public class MyCursorWrapper extends CursorWrapper {
    public MyCursorWrapper(Cursor cursor) {
        super(cursor);



    }
    public Cardbase getCardbase(){
        String uuid=getString(getColumnIndex(CardsTable.Cols.UUID));
        String jsonstring=getString(getColumnIndex(CardsTable.Cols.JSONSTRING));

        Cardbase cardbase=new Cardbase(UUID.fromString(uuid));
        cardbase.fromJson(jsonstring);

        return cardbase;


    }


}
