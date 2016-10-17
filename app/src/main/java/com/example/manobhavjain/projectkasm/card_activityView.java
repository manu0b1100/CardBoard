package com.example.manobhavjain.projectkasm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;
import java.util.UUID;

/**
 * Created by Manobhav Jain on 8/11/2016.
 */
/*public class card_activityView extends AppCompatActivity {


    private ViewPager viewPager;
    private FragmentStatePagerAdapter pagerAdapter;
    private List<Cardbase> cardbaseList;
    private int pos;


    public static Intent newInstance(Context context, UUID id){
        Intent i=new Intent(context,card_activityView.class);
        i.putExtra("UUID1",id);
        return i;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_activity);

        UUID uuid=(UUID)getIntent().getSerializableExtra("UUID1");
        cardbaseList=CardsLab.get(card_activityView.this).getAllIndivCards();

        viewPager=(ViewPager)findViewById(R.id.card_act_pager);


        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                pos=position;

                return Frag_CardDetailView.newInstance(cardbaseList.get(position).getId());
            }

            @Override
            public int getCount() {
                return cardbaseList.size();
            }
        });
        for (int i = 0; i < cardbaseList.size(); i++) {
            if (cardbaseList.get(i).getId().equals(uuid)) {
                viewPager.setCurrentItem(i);
                break;
            }

        }



    }



}*/
