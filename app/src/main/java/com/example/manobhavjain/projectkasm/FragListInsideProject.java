package com.example.manobhavjain.projectkasm;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
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
    List<Cardbase> cardbases;
    long prevsec;
    PagerChanger pagerChanger;



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

        pagerChanger=(ProjectActivity)getActivity();
        Calendar c=Calendar.getInstance();
        prevsec=c.getTimeInMillis();
        listofcards=getArguments().getStringArrayList("cardlist");
        if(listofcards==null){
            listofcards=new ArrayList<String>();

        }


        Log.i("fraginsideproject","list recvd"+listofcards);
        View v = inflater.inflate(R.layout.fraglistinsideproject, container, false);
        listRecycle = (RecyclerView) v.findViewById(R.id.listRecycle);
        listRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        listRecycle.setOnDragListener(new MyDragListener());

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
        ItemTouchHelper.Callback callback=new SimpleItemTouchHelperCallback(listViewAdapter);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(listRecycle);

        return v;

    }


    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;

        private String uuid;

        public ListViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new MyTouchListener());
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), title.getText(), Toast.LENGTH_LONG).show();
            Intent i = card_activityEdit.newInstanceEmpty(getActivity(), uuid);
            startActivity(i);


        }
    }

    private class ListViewAdapter extends RecyclerView.Adapter<ListViewHolder> implements ItemTouchHelperAdapter {
        private List<Cardbase> notes;
        private ArrayList<String>notesstring;

        public ListViewAdapter(List<Cardbase> notes) {
            this.notes = notes;
        }

        public List<Cardbase> getNotes() {
            return notes;
        }

        public ArrayList<String> getNotesstring() {
            return notesstring;
        }

        public void setNotesstring(ArrayList<String> notesstring) {
            this.notesstring = notesstring;
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
            holder.itemView.setTag(position);

        }

        @Override
        public void onItemMoved(int from, int to) {

        }

        @Override
        public void onItemSwiped(int position) {
            listofcards.remove(position);
            notifyItemRemoved(position);


        }
    }

    private void getDataupdateUI() {
        cardbases = CardsLab.get(getActivity()).getListCards(listofcards);

        if (listViewAdapter == null) {
            listViewAdapter = new ListViewAdapter(cardbases);
            listRecycle.setAdapter(listViewAdapter);
        } else
            listViewAdapter.setNotes(cardbases);
        listViewAdapter.setNotesstring(listofcards);
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
    private class MyTouchListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View view) {
            ClipData data = ClipData.newPlainText("bhainsi", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            //view.setVisibility(View.INVISIBLE);
            return true;
        }

    }

    class MyDragListener implements View.OnDragListener {
        //Drawable enterShape = getResources().getDrawable(
        //R.drawable.shape_droptarget);
        //Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            Display dis=getActivity().getWindowManager().getDefaultDisplay();
            Point m=new Point();
            dis.getSize(m);
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.invalidate();
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.invalidate();


                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    Calendar c=Calendar.getInstance();
                    long currsec=c.getTimeInMillis();
                    if((currsec-prevsec)>2000) {
                        Log.d("manobhav",currsec+"curr");

                        if ((m.x - event.getX()) < 50) {
                            Log.d("manobhav", "rightedgerec");
                            pagerChanger.pagerright();

                        }
                        if (event.getX() < 50) {
                            Log.d("manobhav", "leftedgerec");
                            pagerChanger.pagerleft();
                        }
                        prevsec=c.getTimeInMillis();
                        Log.d("manobhav",prevsec+"prev");

                    }


                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    View view = (View) event.getLocalState();

                    view.setVisibility(View.VISIBLE);


                    // Dropped, reassign View to ViewGroup
                    //ClipData.Item item = event.getClipData().getItemAt(0);

                    RecyclerView manu = (RecyclerView) view.getParent();
                    ListViewAdapter manua = (ListViewAdapter) manu.getAdapter();

                    // Gets the text data from the item.
                    //String dragData = item.getText().toString();
                    listofcards.add(manua.getNotes().get((int) view.getTag()).getId());
                    cardbases.add(manua.getNotes().get((int) view.getTag()));
                    listViewAdapter.notifyDataSetChanged();
                    manua.getNotes().remove((int) view.getTag());
                    manua.getNotesstring().remove((int) view.getTag());
                    manua.notifyDataSetChanged();

                    //Log.d("manobhav",dragData);

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        }
    }

}
