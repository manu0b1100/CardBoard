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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Manobhav Jain on 9/25/2016.
 */
public class FragListInsideProject extends Fragment{
    private RecyclerView listRecycle;
    private ListViewAdapter listViewAdapter;
    private FloatingActionButton fab;
    private ArrayList<String>listofcards;
    private UUID projid;
    private int pos;


    public static FragListInsideProject newInstance(UUID projid,int position,ArrayList<String>cards){
        FragListInsideProject f=new FragListInsideProject();
        Bundle bundle=new Bundle();
        bundle.putSerializable("projid",projid);
        bundle.putInt("position",position);
        bundle.putStringArrayList("cardlist",cards);

        f.setArguments(bundle);
        return f;

    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i("manobhav", "oncreateview entered");
        projid=(UUID)getArguments().getSerializable("projid");
        pos=getArguments().getInt("position");

        //Log.i("mannu2","size "+project.getListInsideProjects().size());

        listofcards=getArguments().getStringArrayList("cardlist");
        if(listofcards==null){
            listofcards=new ArrayList<String>();

        }


        Log.i("fraginsideproject","list recvd"+listofcards);
        View v = inflater.inflate(R.layout.fraglistinsideproject, container, false);
        listRecycle = (RecyclerView) v.findViewById(R.id.listRecycle);
        listRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        fab = (FloatingActionButton) v.findViewById(R.id.floatingbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Cardbase cardbase = new Cardbase();
        listofcards.add(cardbase.getId().toString());
        CardsLab.get(getActivity()).addnote(cardbase);
        Intent i = card_activityEdit.newInstanceEmpty(getActivity(), cardbase.getId());
        startActivity(i);

            }
        });
        getDataupdateUI();

        return v;

    }


    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;

        private UUID uuid;

        public ListViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);

            itemView.setOnClickListener(this);
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), title.getText(), Toast.LENGTH_LONG).show();
            Intent i = card_activityEdit.newInstanceEmpty(getActivity(), uuid);
            startActivity(i);


        }
    }

    private class ListViewAdapter extends RecyclerView.Adapter<ListViewHolder> {
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
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.frag_list_detail, parent, false);
            return new ListViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ListViewHolder holder, int position) {

            holder.title.setText(notes.get(position).getDatabase().get(0).getDataString());

            holder.setUuid(notes.get(position).getId());

        }
    }

    private void getDataupdateUI() {
        List<Cardbase> cardbases = CardsLab.get(getActivity()).getListCards(listofcards);

        if (listViewAdapter == null) {
            listViewAdapter = new ListViewAdapter(cardbases);
            listRecycle.setAdapter(listViewAdapter);
        } else
            listViewAdapter.setNotes(cardbases);
        listViewAdapter.notifyDataSetChanged();


    }

    @Override
    public void onPause() {
        Log.i("fraglistinsideproject","list is "+listofcards);
        ProjectsLab.get(getActivity()).updateProjectList(projid,pos,listofcards,getActivity());
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataupdateUI();
    }

}
