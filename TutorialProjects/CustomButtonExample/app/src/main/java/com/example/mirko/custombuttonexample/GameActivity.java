package com.example.mirko.custombuttonexample;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LobbyFragment fragment = new LobbyFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slide_out_right);
        fragmentTransaction.add(R.id.mainViewGroup, fragment);
        fragmentTransaction.commit();

    }
}
