package com.example.manobhavjain.projectkasm;

import android.os.Bundle;

import com.semantive.waveformandroid.waveform.WaveformFragment;

/**
 * Created by Manobhav Jain on 10/10/2016.
 */
public class CustomWaveFragment extends WaveformFragment {

    public static CustomWaveFragment newInstance(String filepath) {

        Bundle args = new Bundle();
        args.putString("filepath",filepath);

        CustomWaveFragment fragment = new CustomWaveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getFileName() {
        String s=getArguments().getString("filepath");
        return s;
    }
}
