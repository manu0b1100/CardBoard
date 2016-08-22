package com.example.manobhavjain.projectkasm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Manobhav Jain on 8/11/2016.
 */
public class card_activityEdit extends AppCompatActivity implements VoiceRecorderFragmentDialog.VoicerecorderListener{

    //private File mPhotoFile;
    private Uri imguri;
    private RecyclerView recyclerView;
    private AdapterClass manuAdapter;
    private ArrayList<Data> myObjects;

    private static final int REQUEST_PHOTO=0;
    private static final int REQUEST_GALLERY=1;
    private Cardbase cardbase;
    private MediaPlayer mediaPlayer;

    public static Intent newInstanceEmpty(Context context, UUID uuid){
        Intent i=new Intent(context,card_activityEdit.class);
        i.putExtra("UUID",uuid);
        return i;

    }

    @Override
    public void onFinishRecording(String uri) {
        Log.i("manobhav","audio path is "+uri);
        myObjects.add(new Data(uri,Constants.AUDIONOTE));
        manuAdapter.notifyItemInserted(myObjects.size());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fresco.initialize(card_activityEdit.this);
        setContentView(R.layout.frag_card_edit);
        UUID uuid=(UUID)getIntent().getSerializableExtra("UUID");
        cardbase=CardsLab.get(this).getCrime(uuid);
        myObjects=cardbase.getData();
        if(myObjects.size()<=1)
        myObjects.add(new Data("",Constants.NOTE));
        recyclerView=(RecyclerView)findViewById(R.id.act_edit_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(card_activityEdit.this));
        manuAdapter=new AdapterClass(myObjects);
        recyclerView.setAdapter(manuAdapter);







    }

    @Override
    protected void onPause() {
        super.onPause();
        cardbase.setData(myObjects);
        CardsLab.get(card_activityEdit.this).updateCard(cardbase);
    }




    private class AdapterClass extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private ArrayList<Data> manuObjects;

        public AdapterClass(ArrayList<Data> manuObjects) {
            this.manuObjects = manuObjects;
        }


        private class AudioViewHolder extends RecyclerView.ViewHolder{
            private Button PLbutton;

            public AudioViewHolder(View itemView) {
                super(itemView);
                PLbutton=(Button)itemView.findViewById(R.id.PLbutton);
            }

            public Button getPLbutton() {
                return PLbutton;
            }
        }



        private class TitleViewHolder extends RecyclerView.ViewHolder{
            private EditText title;

            public TitleViewHolder(View itemView) {
                super(itemView);
                title=(EditText)itemView.findViewById(R.id.data);
                title.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        myObjects.get(getAdapterPosition()).setDataString(charSequence.toString());
                        //notifyItemChanged(getAdapterPosition());




                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

            }

            public EditText getTitle() {
                return title;
            }

        }
        private class NoteViewHolder extends RecyclerView.ViewHolder{
            private EditText note;

            public NoteViewHolder(View itemView) {
                super(itemView);
                note=(EditText)itemView.findViewById(R.id.note);
                note.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        myObjects.get(getAdapterPosition()).setDataString(charSequence.toString());
                        //notifyItemChanged(getAdapterPosition());


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

            }

            public EditText getNote() {
                return note;
            }

        }

        private class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private ImageView imageView;

            public ImageViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                imageView=(ImageView) itemView.findViewById(R.id.imageView);
            }

            public ImageView getImageView() {
                return imageView;
            }

            @Override
            public void onClick(View view) {
                Intent manu=new Intent();
                manu.setAction(Intent.ACTION_VIEW);
                manu.setDataAndType(Uri.parse(manuObjects.get(getAdapterPosition()).getDataString()),"image/*");
                startActivity(manu);

            }
        }


        @Override
        public int getItemCount() {
            return manuObjects.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            RecyclerView.ViewHolder viewHolder;
            LayoutInflater inflater=LayoutInflater.from(parent.getContext());
            switch (viewType){
                case Constants.TITLE:
                    View view1 = inflater.inflate(R.layout.title_layout, parent, false);
                    viewHolder=new TitleViewHolder(view1);
                    break;
                case Constants.IMAGE:
                    View view2=inflater.inflate(R.layout.image_layout,parent,false);
                    viewHolder=new ImageViewHolder(view2);
                    break;
                case Constants.NOTE:
                    View view3 = inflater.inflate(R.layout.note_layout, parent, false);
                    viewHolder=new NoteViewHolder(view3);
                    break;
                case Constants.AUDIONOTE:
                    View view4=inflater.inflate(R.layout.play_record,parent,false);
                    viewHolder=new AudioViewHolder(view4);
                    break;

                default:
                    View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                    viewHolder = new TitleViewHolder(v);
                    break;

            }




            return viewHolder;


        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch(holder.getItemViewType()){
                case Constants.TITLE:
                    TitleViewHolder vh1=(TitleViewHolder)holder;
                    configurevh1(vh1,position);
                    break;
                case Constants.IMAGE:
                    ImageViewHolder vh2=(ImageViewHolder)holder;
                    configurevh2(vh2,position);
                    break;
                case Constants.NOTE:
                    NoteViewHolder nvh3=(NoteViewHolder)holder;
                    configurenvh3(nvh3,position);
                    break;
                case Constants.AUDIONOTE:
                    AudioViewHolder avh=(AudioViewHolder)holder;
                    configurenavh(avh,position);
                default:
            }




        }

        @Override
        public int getItemViewType(int position) {

            if(manuObjects.get(position).getTYPE()==Constants.TITLE)
                return Constants.TITLE;
            else if(manuObjects.get(position).getTYPE()==Constants.NOTE)
                return Constants.NOTE;
            else if(manuObjects.get(position).getTYPE()==Constants.IMAGE)
                return Constants.IMAGE;
            else if(manuObjects.get(position).getTYPE()==Constants.AUDIONOTE)
                return Constants.AUDIONOTE;


            return -1;
        }
        private void configurevh1(TitleViewHolder vh1, int position) {
            Data data = manuObjects.get(position);
            if (data != null) {
                vh1.getTitle().setText(data.getDataString());
            }
        }

        private void configurevh2(ImageViewHolder vh2,int position) {
            Log.i("manobhav","the path is"+Uri.parse(manuObjects.get(position).getDataString()));

            Picasso.with(card_activityEdit.this).load(Uri.parse(manuObjects.get(position).getDataString())).resize(300,300).centerCrop().into( vh2.getImageView());

            //vh2.getImageView().setImageURI(Uri.parse(manuObjects.get(position).getDataString()));

        }
        private void configurenvh3(NoteViewHolder nvh3, int position){
            Data data = manuObjects.get(position);
            if (data != null) {
                nvh3.getNote().setText(data.getDataString());
            }

    }
        private void configurenavh(AudioViewHolder avh, final int position){

            avh.getPLbutton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(card_activityEdit.this, "play  button dabaya", Toast.LENGTH_SHORT).show();
                    mediaPlayer = new MediaPlayer();
                    try{
                        mediaPlayer.setDataSource(manuObjects.get(position).getDataString());
                        mediaPlayer.prepare();}

                    catch (Exception e){
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                }
            });
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.card_activity_edit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_new_item:
                addnew();
                return true;
            case R.id.menu_item_addpic:
                addpic();
                return true;
            case R.id.menu_item_addpicgallery:
                addpicgal();
                return true;
            case R.id.menu_item_addvoicenote:
                addvoicenote();
                return true;



            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void addvoicenote(){
        VoiceRecorderFragmentDialog vrfd=new VoiceRecorderFragmentDialog();
        vrfd.setVoicerecorderListener(this);
        vrfd.show(getSupportFragmentManager(),"fragment_alert");




    }
    public void addpicgal(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(galleryIntent,"lele pic"),REQUEST_GALLERY);
    }
    public void addpic(){


        Intent captureImage=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        captureImage.putExtra(MediaStore.EXTRA_OUTPUT,setImageUri());
        startActivityForResult(captureImage,REQUEST_PHOTO);
    }
    public void addnew(){

        myObjects.add(new Data("",Constants.NOTE));
        manuAdapter.notifyItemInserted(myObjects.size());
    }
    public Uri setImageUri() {
        // Store image in dcim
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "image" + new Date().getTime() + ".jpg");
        imguri = Uri.fromFile(file);
        return imguri;


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("manobhav","activity destroyed");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(resultCode!=Activity.RESULT_CANCELED) {
            if (requestCode == REQUEST_PHOTO) {
                //Log.i("manobhav","the cam path is"+imguri.toString());
                myObjects.add(new Data(imguri.toString(), Constants.IMAGE));

                manuAdapter.notifyItemInserted(myObjects.size());

            }
            else if(requestCode==REQUEST_GALLERY){
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();


                myObjects.add(new Data("file://"+imgDecodableString, Constants.IMAGE));

                manuAdapter.notifyItemInserted(myObjects.size());

            }
        }


    }



}
