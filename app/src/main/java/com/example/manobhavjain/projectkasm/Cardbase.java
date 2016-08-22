package com.example.manobhavjain.projectkasm;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

/**
* Created by Manobhav Jain on 8/11/2016.
*/
public class Cardbase {

   private UUID uuid;
   private ArrayList<Data> data=new ArrayList<>();



    public Cardbase() {
        this.uuid=UUID.randomUUID();
        data.add(new Data("",Constants.TITLE));
    }

    public Cardbase(UUID uuid) {
        this.uuid = uuid;
        data.add(new Data("",Constants.TITLE));
    }

    public UUID getId() {
        return uuid;
    }


    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public String getPhotoFilename(){

        return "IMG_"+UUID.randomUUID().toString()+".jpg";
    }

    public String toJSON(){
        Gson gson=new Gson();
        Type type=new TypeToken<ArrayList<Data>>(){}.getType();
        String json=gson.toJson(data,type);
        return json;
    }
    public void fromJson(String json){
        Gson gson=new Gson();
        Type type=new TypeToken<ArrayList<Data>>(){}.getType();
        data=gson.fromJson(json,type);

    }


}

class Data {
    private String data;
    private final int TYPE;

    public Data(String data, int TYPE) {
        this.data = data;
        this.TYPE=TYPE;

    }

    public void setDataString(String data) {
        this.data = data;
    }

    public String getDataString() {
        return data;
    }

    public int getTYPE() {
        return TYPE;
    }
}


