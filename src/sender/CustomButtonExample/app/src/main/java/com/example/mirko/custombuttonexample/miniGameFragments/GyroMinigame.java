package com.example.mirko.custombuttonexample.miniGameFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mirko.custombuttonexample.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GyroMinigame extends Fragment {


    public GyroMinigame() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.gyro_mini_game, container, false);
    }

}