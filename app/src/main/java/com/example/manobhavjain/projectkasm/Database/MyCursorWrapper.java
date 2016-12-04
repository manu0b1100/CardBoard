package com.example.manobhavjain.projectkasm.Database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Pair;

import com.example.manobhavjain.projectkasm.Cardbase;
import com.example.manobhavjain.projectkasm.Database.DbSchema.CardsTable;
import com.example.manobhavjain.projectkasm.Database.DbSchema.ProjectTable;
import com.example.manobhavjain.projectkasm.Project;

import java.util.Date;
import java.util.UUID;


/**
 * Created by Manobhav Jain on 8/11/2016.
 */
public class MyCursorWrapper extends CursorWrapper {
    public MyCursorWrapper(Cursor cursor) {
        super(cursor);



    }
    public Pair<String,Integer> getCardMap(){
        String uuid=getString(getColumnIndex(CardsTable.Cols.UUID));
        Integer change=getInt(getColumnIndex(CardsTable.Cols.CHANGE));

        return Pair.create(uuid,change);

    }
    public String getCardid(){
        String uuid=getString(getColumnIndex(CardsTable.Cols.UUID));
        return uuid;


    }
    public Cardbase getCardbase(){
        String uuid=getString(getColumnIndex(CardsTable.Cols.UUID));
        String jsonstring=getString(getColumnIndex(CardsTable.Cols.JSONSTRING));

        int ind=getInt(getColumnIndex(CardsTable.Cols.INDIVIDUAL));
        int bc=getInt(getColumnIndex(CardsTable.Cols.BACKCOLOR));
        int change=getInt(getColumnIndex(CardsTable.Cols.CHANGE));

        Cardbase cardbase=new Cardbase(uuid);
        cardbase.fromJson(jsonstring);
        cardbase.setIndividual(ind);
        cardbase.setBackcolor(bc);
        cardbase.setChange(change);

        return cardbase;


    }
    public Project getProject(){
        String uuid=getString(getColumnIndex(ProjectTable.Cols.PROJUUID));
        String jsonstring=getString(getColumnIndex(ProjectTable.Cols.PROJJSONSTRING));
        String title=getString(getColumnIndex(ProjectTable.Cols.PROJTITLE));

       Project project=new Project(UUID.fromString(uuid));
        project.fromJSON(jsonstring);
        project.setTitle(title);

        return project;

    }


}
