package com.example.mirko.partycast.miniGameFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mirko.partycast.R;



/**
 * A simple {@link Fragment} subclass.
 */
public class MicMinigame extends Fragment {


    public MicMinigame() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.mic_mini_game, container, false);
    }

}
