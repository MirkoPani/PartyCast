package com.example.mirko.custombuttonexample;

import android.content.Intent;
import android.util.Log;

import java.util.Observable;
import java.util.Observer;

import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.cast.games.GameManagerState;

import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by nicola on 06/06/2017.
 */

public class PartyCastModel implements GameManagerClient.Listener {

    @Override
    public void onStateChanged(GameManagerState gameManagerState, GameManagerState gameManagerState1) {
        Log.d(TAG,"StateChanged from "+gameManagerState+" to "+gameManagerState1);
    }

    @Override
    public void onGameMessageReceived(String s, JSONObject jsonObject) {
        Log.d(TAG,"Message received: "+jsonObject.toString());
    }

}
