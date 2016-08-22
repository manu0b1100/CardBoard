package com.example.manobhavjain.projectkasm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.text.TextDirectionHeuristicCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by Manobhav Jain on 8/15/2016.
 */
/*
public class testActivity extends AppCompatActivity {
    private TextView textViewtitle;
    private TextView textViewdetails;

    public static Intent newInstance(Context context, UUID uuid){
        Intent i=new Intent(context,testActivity.class);
        i.putExtra("UUID1",uuid);
        return i;



    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_carddetail);
        textViewtitle=(TextView) findViewById(R.id.textViewtitle);
        textViewdetails=(TextView) findViewById(R.id.textViewdetails);
        UUID uuid=(UUID)getIntent().getSerializableExtra("UUID1");

        Cardbase cardbase=CardsLab.get(this).getCrime(uuid);
        textViewtitle.setText(cardbase.getTitle());
        textViewdetails.setText(cardbase.getNote());


    }
}
*/