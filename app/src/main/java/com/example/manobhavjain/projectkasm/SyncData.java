package com.example.manobhavjain.projectkasm;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class SyncData {

   private List<Cardbase> manu;
    private ArrayList<Project>manuProj;
    private ArrayList<StatusData> statusData=new ArrayList<>();
    private  Context context;
    private int flag1,flag2;
    private DownloadManager downloadManager;
    private StorageReference mStorageRef;

    public SyncData(Context context) {
        this.context = context;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        flag1=flag2=0;
    }





    /*public void startUpdatingProjects(){
        getProjectsFromServer();
    }*/
    public void startUpdatingCards() {
        getCardsFromServer();
       // List<Cardbase> localcards = CardsLab.get(context).getAllIndivCards();


        //Log.d("manobhav","upload ho gya");
    }

    //https://api.myjson.com/bins/nt0q
    //192.168.43.50:8080/api/kamaljeet/
    //api.myjson.com/bins/3fbz5
    private void getCardsFromServer(){

        StringRequest request=new StringRequest("https://api.myjson.com/bins/4vp9z", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("manobhav",response);
                Gson gson = new Gson();
                Type type = new TypeToken<List<Cardbase>>() {
                }.getType();
                Log.d("manobhav","getcardsfromserver");
                manu = gson.fromJson(response, type);
                //Log.i("getcard",manu.get(0).getId());
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
        StringRequest request=new StringRequest("https://api.myjson.com/bins/4vp9z", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("manobhav",response);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<StatusData>>() {}.getType();
                statusData = gson.fromJson(response, type);
                Log.d("manobhav","getstatusfromserver");

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
        statusData.add(new StatusData());
        statusData.get(0).setCard_id(new ArrayList<CardidObject>());
        statusData.get(0).getCard_id().add(new CardidObject("manobhav"));
            cardids.removeAll(statusData.get(0).getCard_id());//insert elements of list A
           statusData.get(0).getCard_id().removeAll(tempCardids);

        //Log.d("getdifference",cardids.get(0).getKey());
        Log.d("manobhav","getdifference");


        IUDinlocaldatabase(cardids, statusData.get(0).getCard_id());




    }

    private void IUDinlocaldatabase(ArrayList<CardidObject> listA, ArrayList<CardidObject> listB){



        //HashMap<String,Integer> localcardsmap=CardsLab.get(context).getCardMapping();




        downloadManager= (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);

        for(CardidObject temp:listB){
            if(!temp.getKey().equals("manobhav"))
            CardsLab.get(context).deleteNote(temp.getKey());
        }



        for(Cardbase card:manu) {
            Log.d("pehla manu card",card.getId());
            boolean a=listA.contains(new CardidObject(card.getId()));
            Log.d("boolean",""+a);
            if (listA.contains(new CardidObject(card.getId()))) {

                addCard(card);

            }


            /*else{
                if(card.getChange()>localcardsmap.get(card.getId())){
                    CardsLab.get(context).deleteNote(card);

                    addCard(card);

                }
                else if(card.getChange()<localcardsmap.get(card.getId())){
                manu.add(card);
                    localcards.add(CardsLab.get(context).getCard(card.getId()));

                }
            }*/
        }


        List<Cardbase> localcards=CardsLab.get(context).getAllIndivCards();
        List<Cardbase>templocalcards=new ArrayList<>(localcards);
        List<Cardbase>tempmanu=new ArrayList<>(manu);
        localcards.removeAll(manu);//cards to be added
        //manu.removeAll(templocalcards);// cards to be deleted
        tempmanu.removeAll(templocalcards);
        for(Cardbase card:localcards){
            for(final Data data:card.getDatabase()){
                if(data.getType()==Constants.IMAGE||data.getType()==Constants.FILE) {


                    try {
                        String uploadId = UUID.randomUUID().toString();

                        Log.d("manobhav", "upload entered");
                        Log.d("manobhav", data.getDataString().substring(7));

                        //Creating a multi part request
                        new MultipartUploadRequest(context, uploadId, "http://192.168.1.102/upload.php")
                                .addFileToUpload(data.getDataString().substring(7), "upload_photo") //Adding file
                                .addParameter("upload_photo", "manobhav") //Adding text parameter to the request
                                .setNotificationConfig(new UploadNotificationConfig())
                                .setMaxRetries(2)
                                .startUpload(); //Starting the upload

                    } catch (Exception exc) {
                        //Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    data.setDataString(Uri.parse(data.getDataString()).getLastPathSegment());
                }

            }

        }




        /*Gson gson = new Gson();
        Type type = new TypeToken<List<Cardbase>>() {
        }.getType();
        Log.d("manobhav",gson.toJson(localcards,type));*/


        Log.d("manobhav","iudlocaldatabase");


        sendstatusupdate(listA,listB,localcards,tempmanu);
        //sendstatusupdate(listA,listB);

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
    private void sendstatusupdate(final ArrayList<CardidObject>listA,final ArrayList<CardidObject>listB,final List<Cardbase>insertcards,final List<Cardbase>deletecards){
        Log.d("manobhav","sendstatusupdate");

        StringRequest request=new StringRequest(Request.Method.POST,"http://192.168.0.4:8000/api_requests/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("manobhav",response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap();
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<CardidObject>>() {
                }.getType();
                String insertstatus = gson.toJson(listA,type);
                String deletestatus = gson.toJson(listB,type);
                Type type1 = new TypeToken<List<Cardbase>>() {
                }.getType();
                String insertcardstring=gson.toJson(insertcards,type1);
                String deletecardsstring=gson.toJson(deletecards,type1);




                params.put("username","kamaljeet");
                params.put("insertstatus",insertstatus);
                params.put("deletestatus",deletestatus);
                params.put("insertcards",insertcardstring);
                Log.d("manobhav","insert"+insertcardstring);
                params.put("deletecards",deletecardsstring);
                Log.d("manobhav","delete"+deletecardsstring);


                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue=RequestQueueSingleton.getRequestQueue(context);
        requestQueue.add(request);

    }






}
