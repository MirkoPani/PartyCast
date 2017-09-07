package com.example.mirko.partycast.miniGameFragments;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mirko.partycast.GameActivity;
import com.example.mirko.partycast.PartyCastApplication;
import com.example.mirko.partycast.R;
import com.example.mirko.partycast.utils.Orientation;
import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.cast.games.GameManagerState;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class GyroMinigame extends Fragment implements Orientation.Listener {


    private GameManagerClient.Listener mListener = new GyroMinigameListener();

    private Orientation mOrientation;
    private static final String TAG = "GyroMinigame";
    private boolean gameHasStarted;

    public enum movUpDown {
        Up, Down, Stop
    }

    ;

    public enum movLeftRight {
        Left, Right, Stop
    }

    ;
    public movUpDown movUpDownLastValore = movUpDown.Stop;
    public movLeftRight movLeftRightLastValore = movLeftRight.Stop;

    public movUpDown movUpDownCurrentValore;
    public movLeftRight movLeftRightCurrentValore;

    public ConstraintLayout layout;

    public GyroMinigame() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.gyro_mini_game, container, false);


        gameHasStarted=false;
        mOrientation = new Orientation(getActivity());
        layout=(ConstraintLayout) view.findViewById(R.id.gyroMainLayout);


        //Notifichiamo dell'avvenuto cambio
        ((GameActivity)getActivity()).sendMiniGameChangedConfermation();

        return view;
    }

    @Override
    public void onOrientationChanged(float pitch, float roll) {
        //Log.d(TAG, "onOrientationChanged: ");

        if (pitch <= -25) {
            movUpDownCurrentValore = movUpDown.Up;
        } else if (pitch >= 25) {
            movUpDownCurrentValore = movUpDown.Down;
        } else {
            movUpDownCurrentValore = movUpDown.Stop;
        }


        if (roll <= 80 && roll>0) {
            movLeftRightCurrentValore = movLeftRight.Right;
        } else if (roll >= 100) {
            movLeftRightCurrentValore = movLeftRight.Left;
        }else if(roll<=-100)
        {
            movLeftRightCurrentValore = movLeftRight.Right;
        } else if(roll>=-80 && roll<0)
        {
            movLeftRightCurrentValore = movLeftRight.Left;
        }
        else {
            movLeftRightCurrentValore = movLeftRight.Stop;
        }

        //SE uno dei due e' cambiato mandiamo un mess
        if (movUpDownLastValore != movUpDownCurrentValore || movLeftRightLastValore != movLeftRightCurrentValore) {
            sendOrientationMessage(movUpDownCurrentValore, movLeftRightCurrentValore);
            movUpDownLastValore = movUpDownCurrentValore;
            movLeftRightLastValore = movLeftRightCurrentValore;
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        if(gameHasStarted)
            mOrientation.startListening(this);
    }

    public void onResume() {
        if (PartyCastApplication.getInstance().getCastConnectionManager().isConnectedToReceiver()) {
            PartyCastApplication.getInstance().addListenerToEventManager(mListener);
        }
        super.onResume();
    }

    public void onPause() {
        if (PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient() != null) {
            PartyCastApplication.getInstance().removeListenerToEventManager(mListener);
        }
        mOrientation.stopListening();
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        mOrientation.stopListening();
    }

    public void sendOrientationMessage(movUpDown movUpDown, movLeftRight movLeftRight) {
        JSONObject jsonMessage = new JSONObject();
        GameManagerClient gameManagerClient = PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient();
        if (PartyCastApplication.getInstance().getCastConnectionManager().isConnectedToReceiver()) {
            jsonMessage = new JSONObject();
            try {
                jsonMessage.put("movUpDown", movUpDown.name());
                jsonMessage.put("movLeftRight", movLeftRight.toString());
            } catch (JSONException e) {
                Log.e(TAG, "Error creating JSON message", e);
                return;
            }
            PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient().sendGameMessage(jsonMessage);
            Log.d(TAG, "sendOrientationMessage: Sent message orientation");
        }
    }

    //Listener
    private class GyroMinigameListener implements GameManagerClient.Listener {

        @Override
        public void onStateChanged(GameManagerState current, GameManagerState old) {
            Log.d(TAG, "onStateChanged: ");

            //Se stiamo mostrando info a schermo blocchiamo gameplay
            if(current.getGameplayState()==GameManagerClient.GAMEPLAY_STATE_SHOWING_INFO_SCREEN)
            {
                //Unregister shake
                Log.d(TAG, "onStateChanged: info screen");
                mOrientation.stopListening();
            }
            if(current.getGameplayState()==GameManagerClient.GAMEPLAY_STATE_RUNNING)
            {
                Log.d(TAG, "onStateChanged: minigame running");
                mOrientation.startListening(GyroMinigame.this);
                gameHasStarted=true;
            }

        }

        @Override
        public void onGameMessageReceived(String playerId, JSONObject message) {
            Log.d(TAG, "onGameMessageReceived: " + message);
            if (message.has("color")) {

                try {
                    Log.d(TAG, "onGameMessageReceived: colore: "+message.getString("color"));
                    switch(message.getString("color"))
                    {
                        case "area_blu":layout.setBackgroundResource(R.color.blu);break;
                        case "area_arancio":layout.setBackgroundResource(R.color.arancio);break;
                        case "area_crema":layout.setBackgroundResource(R.color.crema);break;
                        case "area_rosa":layout.setBackgroundResource(R.color.rosa);break;
                        default:break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
