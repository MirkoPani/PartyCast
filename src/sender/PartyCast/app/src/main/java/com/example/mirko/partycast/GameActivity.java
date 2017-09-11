package com.example.mirko.partycast;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.cast.games.GameManagerState;

import org.json.JSONException;
import org.json.JSONObject;

import static com.google.android.gms.cast.games.GameManagerClient.LOBBY_STATE_OPEN;

/*
Activity principale usata per gestire i vari minigiochi

 */
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
           PartyCastApplication.getInstance().addListenerToEventManager(mListener);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (gmc != null) {
            Log.d(TAG, "GAMEACTIVITY- " +
                    "" +
                    "onPause:");
//            PartyCastApplication.getInstance().removeListenerToEventManager(mListener);
        }
        super.onPause();
    }

    public void onDestroy(){
        PartyCastApplication.getInstance().removeListenerToEventManager(mListener);
        super.onDestroy();
    }

    //Manda un messaggio al receiver per confermare che il telefono ha cambiato minigioco
    public void sendMiniGameChangedConfermation(){
        JSONObject jsonMessage = new JSONObject();

        if (PartyCastApplication.getInstance().getCastConnectionManager().isConnectedToReceiver()) {
            jsonMessage = new JSONObject();
            try {
                jsonMessage.put("minigamechanged", gmc.getLastUsedPlayerId());
            } catch (JSONException e) {
                Log.e(TAG, "Error creating JSON message", e);
                return;
            }
            PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient().sendGameMessage(jsonMessage);
        }
    }

    private class GameListener implements GameManagerClient.Listener {

        @Override
        public void onStateChanged(GameManagerState currentState, GameManagerState previousState) {
            Log.d(TAG, "onStateChanged: ");
            /*if(currentState.getControllablePlayers().get(0).getPlayerState()==gmc.PLAYER_STATE_PLAYING && previousState.getControllablePlayers().get(0).getPlayerState()==gmc.PLAYER_STATE_READY ){
                Log.d(TAG,"Test di cambio stato-> playing");
            }*/

            //Quando cambiano i dati relativi al gioco aka nuovo minigioco
            if(currentState.hasGameDataChanged(previousState)){
                Log.d(TAG,"Game data changed in " + currentState.getGameData().toString());
                JSONObject jobj = currentState.getGameData();

                try {
                    //se è un minigioco valido
                if(jobj.getString("minigame").equals("none")==false) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainViewGroup, mgm.setMiniGame(jobj.getString("minigame")));
                    fragmentTransaction.commit();
                }
                } catch (JSONException e) {
                    Log.d(TAG,"Errore nel json");
                }
            }


            if(currentState.hasLobbyStateChanged(previousState)){
                Log.d(TAG,"Lobby data changed in " + currentState.getLobbyState());
                //La lobby è stata riaperta, ci andiamo
                if(currentState.getLobbyState()==LOBBY_STATE_OPEN) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainViewGroup, new LobbyFragment());
                    fragmentTransaction.commit();
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
