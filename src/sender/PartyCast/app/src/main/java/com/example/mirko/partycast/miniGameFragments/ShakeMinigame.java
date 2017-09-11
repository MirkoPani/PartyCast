package com.example.mirko.partycast.miniGameFragments;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mirko.partycast.PartyCastApplication;
import com.example.mirko.partycast.R;
import com.example.mirko.partycast.utils.ShakeDetector;
import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.cast.games.GameManagerState;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fragment del minigioco "Shake"
 */

public class ShakeMinigame extends Fragment {

    private static final String TAG = "ShakeMinigame";
    private GameManagerClient.Listener mListener = new ShakeMinigameListener();

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private boolean gameHasStarted;
    MediaPlayer mp;
    Vibrator v;

    public ShakeMinigame() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.shake_mini_game, container, false);


        gameHasStarted=false;

        // ShakeDetector initialization
        mSensorManager = (SensorManager) PartyCastApplication.getInstance().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();

        //mSensorManager.registerListener(mShakeDetector,mAccelerometer,SensorManager.SENSOR_DELAY_GAME);

        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                Log.d(TAG, "onShake: ");
                sendShakeMessage();
                v.vibrate(400);
                mp.start();
            }
        });

        //Vibrazione
        v = (Vibrator) PartyCastApplication.getInstance().getSystemService(Context.VIBRATOR_SERVICE);

        //MediaPlayer
        mp = MediaPlayer.create(PartyCastApplication.getInstance(), R.raw.coin);


        return view;
    }

    //Manda un messaggio per avvisare che siamo l'artista
    private void sendShakeMessage() {
        JSONObject jsonMessage = new JSONObject();
        GameManagerClient gameManagerClient = PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient();
        if (PartyCastApplication.getInstance().getCastConnectionManager().isConnectedToReceiver()) {
            jsonMessage = new JSONObject();
            try {
                jsonMessage.put("shake", gameManagerClient.getLastUsedPlayerId());
            } catch (JSONException e) {
                Log.e(TAG, "Error creating JSON message", e);
                return;
            }
            PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient().sendGameMessage(jsonMessage);
        }
    }


    @Override
    public void onResume() {
        if (PartyCastApplication.getInstance().getCastConnectionManager().isConnectedToReceiver()) {
            PartyCastApplication.getInstance().addListenerToEventManager(mListener);
        }

        registerShakeListener();
        //mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);

        mp = MediaPlayer.create(PartyCastApplication.getInstance(), R.raw.coin);
        super.onResume();

    }

    public void registerShakeListener(){
        if(gameHasStarted)
        {
            mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onPause() {
        if (PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient() != null) {
            PartyCastApplication.getInstance().removeListenerToEventManager(mListener);
        }
        //Unregister shake
        mSensorManager.unregisterListener(mShakeDetector);

        mp.release();
        mp = null;

        super.onPause();

    }

    public void onDestroy() {
        if (mp != null) mp.release();
        mp = null;
        super.onDestroy();
    }

    //Listener usato dal fragment per ricevere messaggi
    private class ShakeMinigameListener implements GameManagerClient.Listener {

        @Override
        public void onStateChanged(GameManagerState current, GameManagerState old) {
            Log.d(TAG, "onStateChanged: ");

            //Se stiamo mostrando info a schermo blocchiamo gameplay
            if(current.getGameplayState()==GameManagerClient.GAMEPLAY_STATE_SHOWING_INFO_SCREEN)
            {
                //Unregister shake
                Log.d(TAG, "onStateChanged: info screen");
                mSensorManager.unregisterListener(mShakeDetector);
            }
            if(current.getGameplayState()==GameManagerClient.GAMEPLAY_STATE_RUNNING)
            {
                Log.d(TAG, "onStateChanged: minigame running");
                mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
                gameHasStarted=true;
            }
        }

        @Override
        public void onGameMessageReceived(String playerId, JSONObject message) {
            Log.d(TAG, "onGameMessageReceived: " + message);
            if (message.has("startGame")) {
                /*mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
                gameHasStarted=true;
            */}
        }
    }

}
