package com.example.manobhavjain.projectkasm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Manobhav Jain on 8/23/2016.
 */
public class ChecklistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<CheckListItemClass> items;
    private Context context;


    public ChecklistAdapter(ArrayList<CheckListItemClass> items,Context context) {
        this.items = items;
        this.context=context;
    }
    private class CheckListItemViewHolder extends RecyclerView.ViewHolder{

        private EditText title;
        private CheckBox isChecked;

        public CheckListItemViewHolder(final View itemView) {
            super(itemView);
            title=(EditText) itemView.findViewById(R.id.checktitle);
            isChecked=(CheckBox)itemView.findViewById(R.id.checkBox);
            isChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    items.get(getAdapterPosition()).setDone(b);

                }
            });
            title.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    items.get(getAdapterPosition()).setItem(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }



        public void setTitle(EditText title) {
            this.title = title;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.checklist_item,parent,false);

        return new CheckListItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CheckListItemViewHolder hold=(CheckListItemViewHolder)holder;
        hold.title.setText(items.get(position).getItem());
        hold.isChecked.setChecked(items.get(position).isDone());

    }
}
