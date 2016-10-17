package com.example.manobhavjain.projectkasm.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

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
    public String getCardid(){
        String uuid=getString(getColumnIndex(CardsTable.Cols.UUID));
        return uuid;


    }
    public Cardbase getCardbase(){
        String uuid=getString(getColumnIndex(CardsTable.Cols.UUID));
        String jsonstring=getString(getColumnIndex(CardsTable.Cols.JSONSTRING));

        int ind=getInt(getColumnIndex(CardsTable.Cols.INDIVIDUAL));
        int bc=getInt(getColumnIndex(CardsTable.Cols.BACKCOLOR));

        Cardbase cardbase=new Cardbase(UUID.fromString(uuid));
        cardbase.fromJson(jsonstring);
        cardbase.setIndividual(ind);
        cardbase.setBackcolor(bc);

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
