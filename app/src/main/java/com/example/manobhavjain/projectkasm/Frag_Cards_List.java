package com.example.manobhavjain.projectkasm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

import co.dift.ui.SwipeToAction;

/**
 * Created by Manobhav Jain on 8/11/2016.
 */
public class Frag_Cards_List extends Fragment {
    private RecyclerView listRecycle;
    private ListViewAdapter  listViewAdapter;
    private SwipeToAction swipeToAction;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i("manobhav","oncreateview entered");
        Fresco.initialize(getActivity());

        View v=inflater.inflate(R.layout.frag_list,container,false);
        listRecycle=(RecyclerView)v.findViewById(R.id.listRecycle);
        listRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));


        getDataupdateUI();

        swipeToAction=new SwipeToAction(listRecycle, new SwipeToAction.SwipeListener<Cardbase>() {

            @Override
            public boolean swipeLeft(final Cardbase card) {
                CardsLab.get(getActivity()).deleteNote(card);
                getDataupdateUI();
                return true;
            }

            @Override
            public boolean swipeRight(Cardbase card) {
                Toast.makeText(getActivity(), "toast right", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public void onClick(Cardbase card) {
                Toast.makeText(getActivity(),card.getDatabase().get(0).getDataString(),Toast.LENGTH_LONG).show();
                Intent i=card_activityEdit.newInstanceEmpty(getActivity(),card.getId());
                startActivity(i);

            }

            @Override
            public void onLongClick(Cardbase itemData) {

            }
        });

        return v;

    }


    private class ListViewHolder extends SwipeToAction.ViewHolder<Cardbase> {

        private TextView title;

        //private UUID uuid;

        public ListViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);

            //.setOnClickListener(this);
        }

        /*public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(),title.getText(),Toast.LENGTH_LONG).show();
            Intent i=card_activityEdit.newInstanceEmpty(getActivity(),uuid);
            startActivity(i);


        }*/
    }

    private class ListViewAdapter extends RecyclerView.Adapter<ListViewHolder>{
        private List<Cardbase> notes;

        public ListViewAdapter(List<Cardbase> notes) {
            this.notes = notes;
        }

        public void setNotes(List<Cardbase> notes) {
            this.notes = notes;
        }

        @Override
        public int getItemCount() {
            return notes.size();
        }

        @Override
        public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            View v=inflater.inflate(R.layout.frag_list_detail,parent,false);
            return new ListViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ListViewHolder holder, int position) {

            Cardbase card=notes.get(position);

            holder.title.setText(notes.get(position).getDatabase().get(0).getDataString());

            //holder.setUuid(notes.get(position).getId());
            holder.data=card;

        }
    }

    private void getDataupdateUI(){
        List<Cardbase>cardbases=CardsLab.get(getActivity()).getAllIndivCards();

        if(listViewAdapter==null) {
            listViewAdapter = new ListViewAdapter(cardbases);
            listRecycle.setAdapter(listViewAdapter);
        }
        else
            listViewAdapter.setNotes(cardbases);
        listViewAdapter.notifyDataSetChanged();


    }

    @Override
    public void onResume() {
        super.onResume();
        getDataupdateUI();
    }



}
