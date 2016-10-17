package com.example.manobhavjain.projectkasm;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Manobhav Jain on 8/29/2016.
 */
public class Project {
    private UUID uuid;
    private String title;
    private ArrayList<ListInsideProject>listInsideProjects=new ArrayList<>();

    public Project() {
        uuid=UUID.randomUUID();
        listInsideProjects.add(new ListInsideProject("List 1"));

    }

    public Project(UUID uuid) {
        this.uuid = uuid;
    }
    public String toJSON(){
        Gson gson=new Gson();
        Type type=new TypeToken<ArrayList<ListInsideProject>>(){}.getType();

        String JSON=gson.toJson(listInsideProjects,type);
        return JSON;


    }
    public void fromJSON(String jsonSring){
        Gson gson=new Gson();
        Type type=new TypeToken<ArrayList<ListInsideProject>>(){}.getType();

        listInsideProjects=gson.fromJson(jsonSring,type);



    }

    public ArrayList<ListInsideProject> getListInsideProjects() {
        return listInsideProjects;
    }

    public void setListInsideProjects(ArrayList<ListInsideProject> listInsideProjects) {
        this.listInsideProjects = listInsideProjects;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
class ListInsideProject{
    private String title;
    private UUID id;
    private ArrayList<String> listofcards=new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ListInsideProject() {
        this.id=UUID.randomUUID();
    }
    public ListInsideProject(String title) {
        this.id=UUID.randomUUID();
        this.title=title;
    }

    public ArrayList<String> getListofcards() {
        return listofcards;
    }

    public void setListofcards(ArrayList<String> listofcards) {
        this.listofcards = listofcards;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
