package com.example.manobhavjain.projectkasm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by Manobhav Jain on 8/11/2016.
 */
/*public class Frag_CardDetailView extends Fragment {
    private TextView textViewtitle;
    private TextView textViewdetails;
    private Cardbase cardbase;


    public static Frag_CardDetailView newInstance(UUID id) {

        Bundle args = new Bundle();
        args.putSerializable("UUID",id);
        Frag_CardDetailView fragment = new Frag_CardDetailView();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag_carddetail,container,false);
        textViewtitle=(TextView) v.findViewById(R.id.textViewtitle);
        textViewdetails=(TextView) v.findViewById(R.id.textViewdetails);
        return v;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewtitle.setText(cardbase.getTitle());
        textViewdetails.setText(cardbase.getNote());

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID uuid=(UUID) getArguments().getSerializable("UUID");
        cardbase=CardsLab.get(getActivity()).getCrime(uuid);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.card_activity_view,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_edit_card:
                Intent i=card_activityEdit.newInstanceEmpty(getActivity(),cardbase.getId());
                startActivity(i);
                return true;
            case R.id.menu_item_share_card:
                sharenote();
                return true;
        }


        return super.onOptionsItemSelected(item);


    }
    private void sharenote(){
        Intent i=new Intent();
        i.setAction(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT,cardbase.getTitle()+"\n"+cardbase.getNote());
        startActivity(Intent.createChooser(i,"Share Note"));


    }
}*/
