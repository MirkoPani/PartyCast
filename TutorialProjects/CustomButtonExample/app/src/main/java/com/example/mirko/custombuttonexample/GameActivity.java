package com.example.mirko.custombuttonexample;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class GameActivity extends GeneralActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Abilitiamo full screen
        setFullScreenListener();

        //Set layout
        setContentView(R.layout.activity_game);


        //Transizione fragments con animazione
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LobbyFragment fragment = new LobbyFragment();
        //fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slide_out_right);
        fragmentTransaction.add(R.id.mainViewGroup, fragment);
        fragmentTransaction.commit();

    }
}
