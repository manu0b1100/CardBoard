package com.example.manobhavjain.projectkasm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.manobhavjain.projectkasm.Database.DatabaseHelper;
import com.example.manobhavjain.projectkasm.Database.DbSchema.CardsTable;
import com.example.manobhavjain.projectkasm.Database.MyCursorWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Manobhav Jain on 8/12/2016.
 */
public class CardsLab {
    public static CardsLab scardslab;

    private Context mcontext;
    private SQLiteDatabase mDatabase;

    public static CardsLab get(Context context){
        if(scardslab==null)
        {scardslab=new CardsLab(context);}
        return scardslab;
    }

    private CardsLab(Context context) {
        this.mcontext = context.getApplicationContext();
        this.mDatabase=new DatabaseHelper(mcontext).getWritableDatabase();
    }

    public void addnote(Cardbase cardbase){
        ContentValues values=getContentValues(cardbase);
        mDatabase.insert(CardsTable.NAME,null,values);

    }
    public static ContentValues getContentValues(Cardbase cardbase){
        ContentValues values=new ContentValues();
        values.put(CardsTable.Cols.UUID,cardbase.getId().toString());
        values.put(CardsTable.Cols.JSONSTRING,cardbase.toJSON());
        return values;
    }

    public List<Cardbase> getAllCards(){
        MyCursorWrapper manuWrapper=queryCards(null,null);
        List<Cardbase>cardbases=new ArrayList<>();

        try{

            manuWrapper.moveToFirst();
            while(!manuWrapper.isAfterLast()){
                cardbases.add(manuWrapper.getCardbase());
                manuWrapper.moveToNext();
            }

        }
        finally {
            manuWrapper.close();
        }

        return cardbases;

    }
    public Cardbase getCrime(UUID id){
        MyCursorWrapper cursor=queryCards(CardsTable.Cols.UUID+" =?",new String[]{
                id.toString()
        });
        try{
            if(cursor.getCount()==0)
            {return null;}

            cursor.moveToFirst();
            return cursor.getCardbase();
        }
        finally {
            cursor.close();
        }



    }

    public File getPhotoFile(Cardbase cardbase){
        File externalFilesDir=mcontext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFilesDir==null){
            return null;
        }
        return new File(externalFilesDir,cardbase.getPhotoFilename());
    }

    private MyCursorWrapper queryCards(String whereClause, String whereArgs[]){
        Cursor cursor=mDatabase.query(CardsTable.NAME,null,whereClause,whereArgs,null,null,null);
        return new MyCursorWrapper(cursor);


    }

    public void updateCard (Cardbase crime){
        String uuidString=crime.getId().toString();
        ContentValues values=getContentValues(crime);

        mDatabase.update(CardsTable.NAME,values,CardsTable.Cols.UUID + "=?",new String[]{uuidString});
    }


}
