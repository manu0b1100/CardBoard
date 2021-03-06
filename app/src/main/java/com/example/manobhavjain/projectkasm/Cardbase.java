package com.example.manobhavjain.projectkasm;

import android.graphics.Color;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

/**
* Created by Manobhav Jain on 8/11/2016.
*/
public class Cardbase {

   private String key;
   private ArrayList<Data> database =new ArrayList<>();
    private int individual=0;
    private int change=0;
    private int backcolor= Color.parseColor("#ffffff");




    public Cardbase() {
        this.key =UUID.randomUUID().toString();
        database.add(new Data("",Constants.TITLE));
    }

    public Cardbase(String key) {
        this.key = key;
        database.add(new Data("",Constants.TITLE));
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }

    public int getBackcolor() {
        return backcolor;
    }

    public void setBackcolor(int backcolor) {
        this.backcolor = backcolor;
    }

    public int getIndividual() {
        return individual;
    }

    public void setIndividual(int individual) {
        this.individual = individual;
    }

    public String getId() {
        return key;
    }


    public ArrayList<Data> getDatabase() {
        return database;
    }

    public void setDatabase(ArrayList<Data> database) {
        this.database = database;
    }



    public String toJSON(){
        Gson gson=new Gson();
        Type type=new TypeToken<ArrayList<Data>>(){}.getType();
        String json=gson.toJson(database,type);
        return json;
    }
    public void fromJson(String json){
        Gson gson=new Gson();
        Type type=new TypeToken<ArrayList<Data>>(){}.getType();
        database =gson.fromJson(json,type);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cardbase cardbase = (Cardbase) o;

        if(this.key.equals(cardbase.key))
            return true;
        else
            return false;

    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}

class Data {
    private String data;
    private final int type;
    private ArrayList<CheckListItemClass>checklist=new ArrayList<>();


    public Data(String data, int TYPE) {
        this.data = data;
        this.type =TYPE;

    }

    public void setDataString(String data) {
        this.data = data;
    }

    public String getDataString() {
        return data;
    }

    public int getType() {
        return type;
    }

    public ArrayList<CheckListItemClass> getChecklist() {
        return checklist;
    }
}

class CheckListItemClass{
    private String item;
    private boolean done;

    public CheckListItemClass(boolean done, String title) {
        this.done = done;
        this.item = title;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}

class CheckList{
    private ArrayList<CheckListItemClass>items=new ArrayList<>();

    public ArrayList<CheckListItemClass> getItems() {
        return items;
    }

    public void setItems(ArrayList<CheckListItemClass> items) {
        this.items = items;
    }

    public String toJson(){
        Gson gson=new Gson();
        Type type=new TypeToken<ArrayList<CheckListItemClass>>(){}.getType();
        String Json=gson.toJson(items,type);
        return Json;
    }
    public void fromJson(String JsonString){
        Gson gson=new Gson();
        Type type=new TypeToken<ArrayList<CheckListItemClass>>(){}.getType();
        items=gson.fromJson(JsonString,type);

    }
}