package com.example.manobhavjain.projectkasm;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Manobhav Jain on 11/18/2016.
 */

public class RequestQueueSingleton {
    public static RequestQueue mRequest;

    public static RequestQueue getRequestQueue(Context context){
        if(mRequest==null)
            mRequest= Volley.newRequestQueue(context.getApplicationContext());
        return mRequest;
    }


}
