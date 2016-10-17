/*package com.example.manobhavjain.projectkasm;

import android.content.ContentValues;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SyncData {
    private


    void sync(){
        List<Cardbase>cards=CardsLab.get(context).getAllIndivCards();

        for(final Cardbase card:cards) {


            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SYNCURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONArray jsonarray = null;
                    try {
                        jsonarray = new JSONArray(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if(jsonarray.getJSONObject(0)==null && jsonarray.getJSONObject(1)==null){
                            copy(a,b);
                            insert(status,a);
                        }
                        else if(jsonarray.getJSONObject(0)==null && jsonarray.getJSONObject(1)!=null){
                            CardsLab.get(context).deleteNote(card);
                            delete(status,a);
                        }
                        else if(jsonarray.getJSONObject(0)!=null && jsonarray.getJSONObject(1)==null){
                            insert(status,a);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request
                    params.put(Config.UUID, card.getId().toString());
                    //returning parameter
                    return params;
                }
            };
        }

    }




}
*/