package com.example.manobhavjain.projectkasm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Manobhav Jain on 8/11/2016.
 */
public class Frag_Cards_List extends Fragment {
    private RecyclerView listRecycle;
    private ListViewAdapter  listViewAdapter;
    private FloatingActionButton fab;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i("manobhav","oncreateview entered");

        View v=inflater.inflate(R.layout.frag_list,container,false);
        listRecycle=(RecyclerView)v.findViewById(R.id.listRecycle);
        listRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        fab=(FloatingActionButton)v.findViewById(R.id.floatingbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cardbase cardbase=new Cardbase();
                CardsLab.get(getActivity()).addnote(cardbase);
                Intent i=card_activityEdit.newInstanceEmpty(getActivity(),cardbase.getId());
                startActivity(i);
            }
        });
        getDataupdateUI();

        return v;

    }


    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView title;

        private UUID uuid;

        public ListViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);

            itemView.setOnClickListener(this);
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(),title.getText(),Toast.LENGTH_LONG).show();
            Intent i=card_activityEdit.newInstanceEmpty(getActivity(),uuid);
            startActivity(i);


        }
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

            holder.title.setText(notes.get(position).getData().get(0).getDataString());

            holder.setUuid(notes.get(position).getId());

        }
    }

    private void getDataupdateUI(){
        List<Cardbase>cardbases=CardsLab.get(getActivity()).getAllCards();

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

    /*private void onRequest(){
        Log.i("manobhav","onrequest entered");

        String url="http://192.168.1.105/notes/getallnote.php";

        StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();
                jsoNallnotes=gson.fromJson(response,JSONallnotes.class);
                Log.i("manobhav",response);
                Log.i("manobhav",jsoNallnotes.toString());
                listViewAdapter=new ListViewAdapter(jsoNallnotes.getCardbase());
                listRecycle.setAdapter(listViewAdapter);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("manobhav","error aa gya");

            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }*/


}
