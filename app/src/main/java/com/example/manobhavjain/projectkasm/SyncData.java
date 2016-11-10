package com.example.manobhavjain.projectkasm;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SyncData {

   private  ArrayList<Cardbase>manu;
    private  Context context;

    public SyncData(Context context) {
        this.context = context;
    }

    void sync(){

        StringRequest request=new StringRequest("https://api.myjson.com/bins/z8tg", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("manobhav",response);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Cardbase>>() {
                }.getType();
                manu = gson.fromJson(response, type);
                Log.i("manobhav",manu.get(0).getId());
                for(Cardbase card:manu){
                    ArrayList<Data>twinkle=card.getDatabase();
                    for(Data data:twinkle){
                        if(data.getType()==Constants.IMAGE){

                            Uri image_uri=Uri.parse(Constants.IMAGEURL+data.getDataString());
                            downloaddata(image_uri,data.getDataString());
                            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), data.getDataString());
                            // new File(Environment.DIRECTORY_DOWNLOADS,)
                            data.setDataString(Uri.fromFile(file).toString());
                            Log.i("manobhav","path stored"+data);
                        }
                        else if(data.getType()==Constants.FILE){
                            Uri image_uri=Uri.parse(Constants.FILEURL+data.getDataString());
                            downloaddata(image_uri,data.getDataString());
                            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), data.getDataString());
                            // new File(Environment.DIRECTORY_DOWNLOADS,)
                            data.setDataString(Uri.fromFile(file).toString());
                            Log.i("manobhav","path stored"+data);

                        }
                        else if(data.getType()==Constants.CHECKLIST){
                            Gson gson1=new Gson();
                            Type type1=new TypeToken<ArrayList<CheckListItemClass>>(){}.getType();
                            String Json=gson1.toJson(data.getChecklist(),type1);
                            data.setDataString(Json);

                        }

                    }

                    CardsLab.get(context).addnote(card);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        RequestQueue requestQueue=Volley.newRequestQueue(context);
        requestQueue.add(request);


        
    }
    private void downloaddata(Uri image_uri,String name){
        String name1="";
        name1=name1.concat(name);
        DownloadManager downloadManager= (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request=new DownloadManager.Request(image_uri);
        request.setTitle("Image Download");
        request.setDestinationInExternalFilesDir(context,Environment.DIRECTORY_DOWNLOADS,name1);

        downloadManager.enqueue(request);

    }





}
