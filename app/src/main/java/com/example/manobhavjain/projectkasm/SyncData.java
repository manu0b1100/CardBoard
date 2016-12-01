package com.example.manobhavjain.projectkasm;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


public class SyncData {

   private  ArrayList<Cardbase>manu;
    private ArrayList<Project>manuProj;
    private ArrayList<StatusData> statusData=new ArrayList<>();
    private  Context context;
    private int flag1,flag2;
    private DownloadManager downloadManager;

    public SyncData(Context context) {
        this.context = context;
        flag1=flag2=0;
    }





    public void startUpdatingProjects(){
        getProjectsFromServer();
    }
    public void startUpdatingCards(){
        getCardsFromServer();

    }

    private void getProjectsFromServer(){
        StringRequest request=new StringRequest("", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Project>>() {

                }.getType();
                manuProj = gson.fromJson(response, type);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue=RequestQueueSingleton.getRequestQueue(context);
        requestQueue.add(request);

    }
    //https://api.myjson.com/bins/nt0q
    //192.168.43.50:8080/api/kamaljeet/
    //api.myjson.com/bins/3fbz5
    private void getCardsFromServer(){

        StringRequest request=new StringRequest("https://api.myjson.com/bins/3fbz5", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("manobhav",response);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Cardbase>>() {
                }.getType();
                manu = gson.fromJson(response, type);
                Log.i("getcard",manu.get(0).getId());
                getStatusFromServer();

                //flag1=1;



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("erorr","error");


            }
        });

        RequestQueue requestQueue=RequestQueueSingleton.getRequestQueue(context);
        requestQueue.add(request);


        
    }
    //https://api.myjson.com/bins/4ajt6
    //192.168.43.50:8080/api_status/kamaljeet/
    //api.myjson.com/bins/3fjox
    private void getStatusFromServer(){
        StringRequest request=new StringRequest("https://api.myjson.com/bins/3fjox", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("manobhav",response);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<StatusData>>() {}.getType();
                statusData = gson.fromJson(response, type);
                //flag2=1;
                getDifference();
                //Log.i("manobhav",manu.get(0).getId());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        RequestQueue requestQueue=RequestQueueSingleton.getRequestQueue(context);
        requestQueue.add(request);
    }
    private void getDifference(){



            ArrayList<CardidObject>cardids=new ArrayList<>();

            for (Cardbase card : manu) {
                cardids.add(new CardidObject(card.getId()));
            }

            ArrayList<CardidObject>tempCardids = new ArrayList<>(cardids);
            cardids.removeAll(statusData.get(0).getCard_id());//insert elements of list A
           statusData.get(0).getCard_id().removeAll(tempCardids);

        //Log.d("getdifference",cardids.get(0).getKey());

        IUDinlocaldatabase(cardids, statusData.get(0).getCard_id());




    }
    private void IUDinlocaldatabase(ArrayList<CardidObject> listA, ArrayList<CardidObject> listB){
        //HashMap<String,Integer> localcards=CardsLab.get(context).getCardMapping();
        downloadManager= (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);

        for(CardidObject temp:listB){
            if(!temp.getKey().equals("manobhav"))
            CardsLab.get(context).deleteNote(temp.getKey());
        }
        //senddeletedcardids(listB);


        for(Cardbase card:manu) {
            Log.d("pehla manu card",card.getId());
            boolean a=listA.contains(new CardidObject(card.getId()));
            Log.d("boolean",""+a);
            if (listA.contains(new CardidObject(card.getId()))) {

                addCard(card);

            }
            //sendinsertedcardids(listA);

            /*else{
                if(card.getChange()>localcards.get(card.getId())){
                    CardsLab.get(context).deleteNote(card);

                    addCard(card);

                }
            }*/
        }

    }
    private void addCard(Cardbase card){
        Log.d("addcard called",card.getId());
        ArrayList<Data> twinkle = card.getDatabase();
        for (Data data : twinkle) {
            if (data.getType() == Constants.IMAGE || data.getType() == Constants.FILE) {


                Uri image_uri = Uri.parse(Constants.IMAGEURL + data.getDataString().substring(1));
                downloaddata(image_uri, data.getDataString().substring(1));
                File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), data.getDataString().substring(1));
                // new File(Environment.DIRECTORY_DOWNLOADS,)
                data.setDataString(Uri.fromFile(file).toString());
                Log.i("manobhav", "path stored" + data);
            } else if (data.getType() == Constants.CHECKLIST) {
                Gson gson1 = new Gson();
                Type type1 = new TypeToken<ArrayList<CheckListItemClass>>() {}.getType();
                String Json = gson1.toJson(data.getChecklist(), type1);
                data.setDataString(Json);

            }

        }
        CardsLab.get(context).addnote(card);

    }
    private void downloaddata(Uri image_uri,String name){
        String name1="";
        name1=name1.concat(name);
        DownloadManager.Request request=new DownloadManager.Request(image_uri);
        request.setTitle("Image Download");
        request.setDestinationInExternalFilesDir(context,Environment.DIRECTORY_DOWNLOADS,name1);

        downloadManager.enqueue(request);

    }





}
