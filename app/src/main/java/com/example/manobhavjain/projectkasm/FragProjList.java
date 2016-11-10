package com.example.manobhavjain.projectkasm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import co.dift.ui.SwipeToAction;

/**
 * Created by Manobhav Jain on 8/29/2016.
 */
public class FragProjList extends Fragment{

    private RecyclerView listRecycle;
    private ListViewAdapter  listViewAdapter;
    private SwipeToAction swipeToAction;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i("manobhav","oncreateview entered");

        View v=inflater.inflate(R.layout.frag_list,container,false);
        listRecycle=(RecyclerView)v.findViewById(R.id.listRecycle);
        listRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeToAction=new SwipeToAction(listRecycle, new SwipeToAction.SwipeListener<Project>() {
            @Override
            public void onClick(Project itemData) {
                Toast.makeText(getActivity(),itemData.getTitle(),Toast.LENGTH_LONG).show();
                Intent i=ProjectActivity.newInstanceEmpty(getActivity(),itemData.getUuid());
                startActivity(i);

            }

            @Override
            public boolean swipeLeft(Project itemData) {
                ProjectsLab.get(getActivity()).deleteProject(itemData);
                getDataupdateUI();
                return true;
            }

            @Override
            public boolean swipeRight(Project itemData) {
                Toast.makeText(getActivity(), "toast right", Toast.LENGTH_SHORT).show();
                return true;

            }

            @Override
            public void onLongClick(Project itemData) {

            }
        });

        getDataupdateUI();

        return v;

    }


    private class ListViewHolder extends SwipeToAction.ViewHolder<Project>{

        private TextView title;


        public ListViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.item);


        }




    }

    private class ListViewAdapter extends RecyclerView.Adapter<ListViewHolder>{
        private List<Project> projects;

        public ListViewAdapter(List<Project> projects) {
            this.projects = projects;
        }

        public void setProjects(List<Project> projects) {
            this.projects = projects;
        }

        @Override
        public int getItemCount() {
            return projects.size();
        }

        @Override
        public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            View v=inflater.inflate(R.layout.frag_list_detail,parent,false);
            return new ListViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ListViewHolder holder, int position) {
            Project project=projects.get(position);

            holder.title.setText(projects.get(position).getTitle());
            holder.data=project;


        }
    }

    private void getDataupdateUI(){
        List<Project>projects=ProjectsLab.get(getActivity()).getAllProjects();

        if(listViewAdapter==null) {
            listViewAdapter = new ListViewAdapter(projects);
            listRecycle.setAdapter(listViewAdapter);
        }
        else
            listViewAdapter.setProjects(projects);
        listViewAdapter.notifyDataSetChanged();


    }

    @Override
    public void onResume() {
        super.onResume();
        getDataupdateUI();
    }



}
