package com.example.manobhavjain.projectkasm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.manobhavjain.projectkasm.Database.DatabaseHelper;
import com.example.manobhavjain.projectkasm.Database.DbSchema;
import com.example.manobhavjain.projectkasm.Database.DbSchema.ProjectTable;
import com.example.manobhavjain.projectkasm.Database.MyCursorWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Manobhav Jain on 8/30/2016.
 */
public class ProjectsLab {

    public static ProjectsLab sprojectsLab;

    private Context mcontext;
    private SQLiteDatabase mDatabase;

    public static ProjectsLab get(Context context){
        if(sprojectsLab==null)
        {sprojectsLab=new ProjectsLab(context);}
        return sprojectsLab;
    }

    private ProjectsLab(Context context) {
        this.mcontext = context.getApplicationContext();
        this.mDatabase=new DatabaseHelper(mcontext).getWritableDatabase();
    }
    public void addProject(Project project){
        ContentValues values=new ContentValues();
        values.put(ProjectTable.Cols.PROJUUID,project.getUuid().toString());
        values.put(ProjectTable.Cols.PROJTITLE,project.getTitle());
        values.put(ProjectTable.Cols.PROJJSONSTRING,project.toJSON());
        mDatabase.insert(ProjectTable.NAME,null,values);

    }
    public void deleteProject(Project project){
        mDatabase.delete(ProjectTable.NAME, ProjectTable.Cols.PROJUUID + "=?",new String[]{project.getUuid().toString()});

    }
    public Project getProject(UUID id){
        MyCursorWrapper cursor=queryCards(ProjectTable.Cols.PROJUUID+" =?",new String[]{
                id.toString()
        });
        try{
            if(cursor.getCount()==0)
            {return null;}

            cursor.moveToFirst();
            return cursor.getProject();
        }
        finally {
            cursor.close();
        }



    }
    public List<Project> getAllProjects(){
        MyCursorWrapper manuWrapper=queryCards(null,null);
        List<Project>projects=new ArrayList<>();

        try{

            manuWrapper.moveToFirst();
            while(!manuWrapper.isAfterLast()){
                projects.add(manuWrapper.getProject());
                manuWrapper.moveToNext();
            }

        }
        finally {
            manuWrapper.close();
        }

        return projects;


    }
    private MyCursorWrapper queryCards(String whereClause, String whereArgs[]){
        Cursor cursor=mDatabase.query(ProjectTable.NAME,null,whereClause,whereArgs,null,null,null);
        return new MyCursorWrapper(cursor);


    }
    public void updateProject (Project project){
        String uuidString=project.getUuid().toString();
        ContentValues values=new ContentValues();
        values.put(ProjectTable.Cols.PROJUUID,project.getUuid().toString());
        values.put(ProjectTable.Cols.PROJTITLE,project.getTitle());
        values.put(ProjectTable.Cols.PROJJSONSTRING,project.toJSON());

        mDatabase.update(ProjectTable.NAME,values,ProjectTable.Cols.PROJUUID + "=?",new String[]{uuidString});
    }
    public void updateProjectList (UUID projid,int pos,ArrayList<String>cards,Context context){
        String uuidString=projid.toString();
        Project project=get(context).getProject(projid);
        project.getListInsideProjects().get(pos).setListofcards(cards);
        ContentValues values=new ContentValues();
        values.put(ProjectTable.Cols.PROJUUID,project.getUuid().toString());
        values.put(ProjectTable.Cols.PROJTITLE,project.getTitle());
        values.put(ProjectTable.Cols.PROJJSONSTRING,project.toJSON());

        Log.i("lab","list is "+cards);

        mDatabase.update(ProjectTable.NAME,values,ProjectTable.Cols.PROJUUID + "=?",new String[]{uuidString});
        Log.i("see project",""+get(context).getProject(projid).toJSON());
    }

}
