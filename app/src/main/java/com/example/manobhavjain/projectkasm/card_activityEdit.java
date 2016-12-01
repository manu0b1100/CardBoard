package com.example.manobhavjain.projectkasm;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.ClickEffectType;
import com.nightonke.boommenu.Types.DimType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;
import com.shinelw.library.ColorArcProgressBar;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
    private BoomMenuButton boomMenuButton;


    private Cardbase cardbase;

    public static Intent newInstanceEmpty(Context context, String uuid){
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
        ;
        String uuid=(String)getIntent().getSerializableExtra("UUID");
        cardbase=CardsLab.get(this).getCard(uuid);
        Log.i("lola","oncreate "+cardbase.getBackcolor());

        RelativeLayout lLayout = (RelativeLayout) findViewById(R.id.editlayout);
        lLayout.setBackgroundColor(cardbase.getBackcolor());
        myObjects=cardbase.getDatabase();
        if(myObjects.size()<=1)
        myObjects.add(new Data("",Constants.NOTE));
        boomMenuButton=(BoomMenuButton)findViewById(R.id.boom);
        recyclerView=(RecyclerView)findViewById(R.id.act_edit_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(card_activityEdit.this));
        manuAdapter=new AdapterClass(myObjects);
        recyclerView.setAdapter(manuAdapter);
        ItemTouchHelper.Callback callback=new SimpleItemTouchHelperCallback(manuAdapter);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cardbase.setDatabase(myObjects);
        Log.i("manobhav",myObjects.get(0).getDataString());
        if(myObjects.get(0).getDataString().contentEquals("")){
            CardsLab.get(card_activityEdit.this).deleteNote(cardbase);
        }
        else
        CardsLab.get(card_activityEdit.this).updateCard(cardbase);
    }




    private class AdapterClass extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter{

        private ArrayList<Data> manuObjects;
        private CheckList checkList;
        private ChecklistAdapter checklistAdapter;



        public AdapterClass(ArrayList<Data> manuObjects) {
            this.manuObjects = manuObjects;
        }

        private class CheckListViewHolder extends RecyclerView.ViewHolder{
            private Button AddItemButton;
            private RecyclerView recyclerView;
            private  Button SaveButton;
            private ColorArcProgressBar capb;

            public CheckListViewHolder(View itemView) {
                super(itemView);
                AddItemButton=(Button)itemView.findViewById(R.id.addItemButton);
                recyclerView=(RecyclerView)itemView.findViewById(R.id.recyclerChecklistItem);
                SaveButton=(Button) itemView.findViewById(R.id.Savebutton);
                capb=(ColorArcProgressBar)itemView.findViewById(R.id.bar1);
            }

        }



        private class AudioViewHolder extends RecyclerView.ViewHolder{
            private FrameLayout frame;
            public AudioViewHolder(View itemView) {
                super(itemView);
                frame=(FrameLayout)itemView.findViewWithTag("container1");
                //frame.setId((int)(new Date()).getTime());
                frame.setId(View.generateViewId());

            }

            public FrameLayout getFrame() {
                return frame;
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
        private class FileViewHolder extends RecyclerView.ViewHolder{
            private Button button;

            public FileViewHolder(View itemView) {
                super(itemView);
                button=(Button)itemView.findViewById(R.id.Filebutton);
            }


            public Button getButton() {
                return button;
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
                case Constants.CHECKLIST:
                    View view5=inflater.inflate(R.layout.checklist_container,parent,false);
                    viewHolder=new CheckListViewHolder(view5);
                    break;
                case Constants.FILE:
                    View view6=inflater.inflate(R.layout.file_layout,parent,false);
                    viewHolder=new FileViewHolder(view6);
                    break;

                default:
                    View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                    viewHolder = new TitleViewHolder(v);
                    break;

            }




            return viewHolder;


        }

        @Override
        public void onItemMoved(int from, int to) {
            if (from < to) {
                for (int i = from; i < to; i++) {

                    Collections.swap(manuObjects, i, i + 1);
                }
            } else {
                for (int i = from; i > to; i--) {
                    Collections.swap(manuObjects, i, i - 1);
                }
            }
            notifyItemMoved(from, to);

        }

        @Override
        public void onItemSwiped(int position) {
            manuObjects.remove(position);
            notifyItemRemoved(position);

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
                    break;
                case Constants.CHECKLIST:
                    CheckListViewHolder cvh=(CheckListViewHolder)holder;
                    configurecvh(cvh,position);
                    break;
                case Constants.FILE:
                    FileViewHolder fvh=(FileViewHolder) holder;
                    configurefvh(fvh,position);
                    break;
                default:
            }





        }

        @Override
        public int getItemViewType(int position) {
            int twinkle=manuObjects.get(position).getType();
            if(twinkle==Constants.DESC)
                return Constants.NOTE;
            else
            return twinkle;

        }
        private void configurefvh(FileViewHolder fvh, final int position){
            fvh.button.setText(manuObjects.get(position).getDataString());
            fvh.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MimeTypeMap myMime = MimeTypeMap.getSingleton();
                    Intent newIntent = new Intent(Intent.ACTION_VIEW);

                    String mimeType = myMime.getMimeTypeFromExtension(fileExt(manuObjects.get(position).getDataString()).substring(1));
                    newIntent.setDataAndType(Uri.parse(manuObjects.get(position).getDataString()),mimeType);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                       startActivity(newIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(card_activityEdit.this, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        private String fileExt(String url){
            if (url.indexOf("?") > -1) {
                url = url.substring(0, url.indexOf("?"));
            }
            if (url.lastIndexOf(".") == -1) {
                return null;
            } else {
                String ext = url.substring(url.lastIndexOf(".") + 1);
                if (ext.indexOf("%") > -1) {
                    ext = ext.substring(0, ext.indexOf("%"));
                }
                if (ext.indexOf("/") > -1) {
                    ext = ext.substring(0, ext.indexOf("/"));
                }
                return ext.toLowerCase();

            }
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

            getSupportFragmentManager().beginTransaction()
                    .add(avh.getFrame().getId(), CustomWaveFragment.newInstance(manuObjects.get(position).getDataString()))
                    .commit();

        }
        private void configurecvh(final CheckListViewHolder cvh, final int position){
            checkList=new CheckList();

            if(!manuObjects.get(position).getDataString().equals(""))
            checkList.fromJson(manuObjects.get(position).getDataString());
            checklistAdapter=new ChecklistAdapter(checkList.getItems(),card_activityEdit.this);
            cvh.recyclerView.setLayoutManager(new LinearLayoutManager(card_activityEdit.this));
            cvh.recyclerView.setAdapter(checklistAdapter);
            cvh.AddItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkList.getItems().add(new CheckListItemClass(false,""));
                    checklistAdapter.notifyItemInserted(checkList.getItems().size());
                }
            });
            cvh.SaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myObjects.get(position).setDataString(checkList.toJson());
                    float c=0;
                    ArrayList<CheckListItemClass>items=checkList.getItems();

                    for(CheckListItemClass item:items){
                        if(item.isDone()==true)
                            c++;

                    }
                    Log.i("manobhav","value "+(c/checkList.getItems().size())*100);
                    cvh.capb.setCurrentValues((c/checkList.getItems().size())*100);
                }
            });
            float c=0;
            ArrayList<CheckListItemClass>items=checkList.getItems();

           for(CheckListItemClass item:items){
               if(item.isDone()==true)
                   c++;

           }
            Log.i("manobhav","value "+(c/checkList.getItems().size())*100);
            cvh.capb.setCurrentValues((c/checkList.getItems().size())*100);

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
            case R.id.menu_backcolor_item:
                changeback();
                return true;
            case R.id.menu_del_item:
                CardsLab.get(this).deleteNote(cardbase);
                finish();
                return true;



            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void changeback(){
        ColorPickerDialogBuilder
                .with(card_activityEdit.this)
                .setTitle("Choose color")
                .initialColor(0xffffffff)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        Toast.makeText(card_activityEdit.this, "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        RelativeLayout lLayout = (RelativeLayout) findViewById(R.id.editlayout);
                        lLayout.setBackgroundColor(selectedColor);

                        cardbase.setBackcolor(selectedColor);
                        Log.i("lola","color"+cardbase.getBackcolor());
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();

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
            if (requestCode == Constants.REQUEST_PHOTO) {
                //Log.i("manobhav","the cam path is"+imguri.toString());
                myObjects.add(new Data(imguri.toString(), Constants.IMAGE));

                manuAdapter.notifyItemInserted(myObjects.size());

            }
            else if(requestCode==Constants.REQUEST_GALLERY){
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
            else if(requestCode==Constants.REQUEST_FILE){
                Uri file=data.getData();
                String twinkii[]={MediaStore.MediaColumns.DATA};
                Cursor cursor=getContentResolver().query(file,twinkii,null,null,null);
                cursor.moveToFirst();
                String filestring=cursor.getString(cursor.getColumnIndex(twinkii[0]));
                cursor.close();
                myObjects.add(new Data("file://"+filestring,Constants.FILE));
                manuAdapter.notifyItemInserted(myObjects.size());



            }
        }


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int[][] subButtonColors = new int[4][2];
        subButtonColors[0][0]= ContextCompat.getColor(this, R.color.colorAccent);
        subButtonColors[0][1]=ContextCompat.getColor(this, R.color.colorAccent);
        subButtonColors[1][0]=ContextCompat.getColor(this, R.color.green);
        subButtonColors[1][1]=ContextCompat.getColor(this, R.color.green);
        subButtonColors[2][0]=ContextCompat.getColor(this, R.color.blue);
        subButtonColors[2][1]=ContextCompat.getColor(this, R.color.blue);
        subButtonColors[3][0]=ContextCompat.getColor(this, R.color.orange);
        subButtonColors[3][1]=ContextCompat.getColor(this, R.color.orange);
        /*for (int i = 0; i < 3; i++) {
            subButtonColors[i][1] = ContextCompat.getColor(this, R.color.colorAccent);
            subButtonColors[i][0] = Util.getInstance().getPressedColor(subButtonColors[i][1]);
        }*/
        new BoomMenuButton.Builder()
                .addSubButton(ContextCompat.getDrawable(this, R.drawable.ic_photo_camera_white_24dp),subButtonColors[0],null )
                .addSubButton(ContextCompat.getDrawable(this, R.drawable.ic_playlist_add_check_white_24dp), subButtonColors[1],null)
                .addSubButton(ContextCompat.getDrawable(this, R.drawable.ic_keyboard_voice_white_24dp), subButtonColors[2],null)
                .addSubButton(ContextCompat.getDrawable(this, R.drawable.ic_photo_library_white_24dp), subButtonColors[3],null)
                .addSubButton(ContextCompat.getDrawable(this, R.drawable.ic_photo_library_white_24dp), subButtonColors[3],null)
                .button(ButtonType.CIRCLE)
                .boom(BoomType.PARABOLA)
                .place(PlaceType.CIRCLE_5_1)
                .autoDismiss(true)
                .cancelable(true)
                .dim(DimType.DIM_6)
                .clickEffect(ClickEffectType.RIPPLE)
                .subButtonTextColor(ContextCompat.getColor(this, R.color.darkness))
                .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .init(boomMenuButton);

        boomMenuButton.setOnSubButtonClickListener(new BoomMenuButton.OnSubButtonClickListener() {
            @Override
            public void onClick(int buttonIndex) {
                switch(buttonIndex){
                    case 0:
                        Toast.makeText(card_activityEdit.this, "button 0 clicked", Toast.LENGTH_SHORT).show();
                        Intent captureImage=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        captureImage.putExtra(MediaStore.EXTRA_OUTPUT,setImageUri());
                        startActivityForResult(captureImage,Constants.REQUEST_PHOTO);
                        break;
                    case 1:
                        Toast.makeText(card_activityEdit.this, "button 1 clicked", Toast.LENGTH_SHORT).show();
                        myObjects.add(new Data("",Constants.CHECKLIST));
                        manuAdapter.notifyItemInserted(myObjects.size());
                        break;
                    case 2:
                        Toast.makeText(card_activityEdit.this, "button 2 clicked", Toast.LENGTH_SHORT).show();
                        VoiceRecorderFragmentDialog vrfd=new VoiceRecorderFragmentDialog();
                        vrfd.setVoicerecorderListener(card_activityEdit.this);
                        vrfd.show(getSupportFragmentManager(),"fragment_alert");
                        break;
                    case 3:
                        Toast.makeText(card_activityEdit.this, "button 2 clicked", Toast.LENGTH_SHORT).show();
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(Intent.createChooser(galleryIntent,"Choose pic"),Constants.REQUEST_GALLERY);
                        break;
                    case 4:
                        Toast.makeText(card_activityEdit.this, "button 2 clicked", Toast.LENGTH_SHORT).show();
                        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        fileIntent.setType("file/*");
                        startActivityForResult(Intent.createChooser(fileIntent,"Choose file"),Constants.REQUEST_FILE);
                        break;
                    default:
                        Toast.makeText(card_activityEdit.this, "default clicked", Toast.LENGTH_SHORT).show();
                        break;


                }
            }
        });
    }
}
