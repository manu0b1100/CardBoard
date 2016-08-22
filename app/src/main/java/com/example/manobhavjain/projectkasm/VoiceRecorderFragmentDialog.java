package com.example.manobhavjain.projectkasm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Manobhav Jain on 8/22/2016.
 */
public class VoiceRecorderFragmentDialog extends DialogFragment {
    private Button Rbutton;
    private Button Sbutton;
    private Button Pbutton;
    private static MediaRecorder mediaRecorder;
    private static MediaPlayer mediaPlayer;
    private static String audiopath;
    private boolean isRecording;




    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v= LayoutInflater.from(getActivity()).inflate(R.layout.voice_record,null);
        Rbutton =(Button)v.findViewById(R.id.rbutton);
        Sbutton =(Button)v.findViewById(R.id.sbutton);
        Pbutton=(Button)v.findViewById(R.id.Pbutton);
        audiopath= Environment.getExternalStorageDirectory().getAbsolutePath()+ "/"+UUID.randomUUID()+".3gp";
        Pbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "play  button dabaya", Toast.LENGTH_SHORT).show();
                mediaPlayer = new MediaPlayer();
                try{
                    mediaPlayer.setDataSource(audiopath);
                    mediaPlayer.prepare();}

                catch (Exception e){
                    e.printStackTrace();
                }
                mediaPlayer.start();

            }
        });
        Rbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Record  button dabaya", Toast.LENGTH_SHORT).show();
                isRecording = true;
                try{
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setOutputFile(audiopath);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.prepare();
                    mediaRecorder.start();

                }

                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        Sbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Stop  button dabaya", Toast.LENGTH_SHORT).show();
                if (isRecording)
                {

                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                    isRecording = false;
                } else {
                    mediaPlayer.release();
                    mediaPlayer = null;

                }

            }
        });



        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Record Voice Note")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
    }

}
