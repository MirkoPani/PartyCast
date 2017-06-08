package com.example.mirko.custombuttonexample;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.cast.games.GameManagerState;
import com.google.android.gms.cast.games.PlayerInfo;

import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.fragment;
import static android.drm.DrmStore.Action.PLAY;

public class GameActivity extends GeneralActivity {

    private GameManagerClient.Listener mListener = new GameListener();
    private static final String TAG = "GameActivity";
    private GameManagerClient gmc;
    MiniGameManager mgm;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Abilitiamo full screen
        setFullScreenListener();

        //Set layout
        setContentView(R.layout.activity_game);

        //settiamo il game manager client
        gmc = PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient();

        //creiamo il mini game manager
        mgm = new MiniGameManager();

        //Transizione fragments con animazione

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        LobbyFragment fragment = new LobbyFragment();
        //fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slide_out_right);
        fragmentTransaction.add(R.id.mainViewGroup, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onResume() {
        if (PartyCastApplication.getInstance().getCastConnectionManager().isConnectedToReceiver()) {
            gmc.setListener(mListener);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (gmc != null) {
            gmc.setListener(null);
        }
        super.onPause();
    }

    private class GameListener implements GameManagerClient.Listener {

        @Override
        public void onStateChanged(GameManagerState currentState, GameManagerState previousState) {
            Log.d(TAG, "onStateChanged: ");
            /*if(currentState.getControllablePlayers().get(0).getPlayerState()==gmc.PLAYER_STATE_PLAYING && previousState.getControllablePlayers().get(0).getPlayerState()==gmc.PLAYER_STATE_READY ){
                Log.d(TAG,"Test di cambio stato-> playing");
            }*/
            if(currentState.hasGameDataChanged(previousState)){
                Log.d(TAG,"Game data changed in " + currentState.getGameData().toString());
                JSONObject jobj = currentState.getGameData();
                try {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainViewGroup, mgm.setMiniGame(jobj.getString("minigame")));
                    fragmentTransaction.commit();
                } catch (JSONException e) {
                    Log.d(TAG,"Errore nel json");
                }
            }
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
