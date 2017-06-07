package com.example.mirko.custombuttonexample;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.cast.games.GameManagerState;

import org.json.JSONObject;

public class GameActivity extends GeneralActivity {

    private GameManagerClient.Listener mListener = new GameListener();
    private static final String TAG = "GameActivity";
    
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

    @Override
    public void onResume() {
        if (PartyCastApplication.getInstance().getCastConnectionManager().isConnectedToReceiver()) {
            PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient().setListener(mListener);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        GameManagerClient client = PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient();
        if (client != null) {
            client.setListener(null);
        }
        super.onPause();
    }

    private class GameListener implements GameManagerClient.Listener {

        @Override
        public void onStateChanged(GameManagerState gameManagerState, GameManagerState gameManagerState1) {
            Log.d(TAG, "onStateChanged: ");
        }

        @Override
        public void onGameMessageReceived(String s, JSONObject jsonObject) {
            Log.d(TAG, "onGameMessageReceived: ");
            if(jsonObject.has("host")){
                Log.d(TAG, "onGameMessageReceived: Siamo diventati host!");
            PartyCastApplication.getInstance().getModel().setHost(true);
            }
        }
    }

}
