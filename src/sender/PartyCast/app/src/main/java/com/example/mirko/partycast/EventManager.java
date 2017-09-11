package com.example.mirko.partycast;

import android.util.Log;

import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.cast.games.GameManagerState;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Creato da Mirko Pani e Nicola Gilberti per il progetto del corso LPSMT 2017
 */

//Classe usata per gestire i vari listener. E' possibile aggiungerli e toglierli, e un evento viene mandato a tutti i listener in ascolto
class EventManager implements GameManagerClient.Listener {
    private static final String TAG = "EventManager";
    private List<GameManagerClient.Listener> registeredListeners = new ArrayList<GameManagerClient.Listener>();

    public void registerListener(GameManagerClient.Listener listener) {
        if (registeredListeners.contains(listener) == false) {
            registeredListeners.add(listener);
            Log.d(TAG, "registerListener: ");
        }
    }

    public void removeListener(GameManagerClient.Listener listener) {
        Log.d(TAG, "removeListener: ");
        registeredListeners.remove(listener);
    }


    @Override
    public void onStateChanged(GameManagerState gameManagerState, GameManagerState gameManagerState1) {
        for (GameManagerClient.Listener listener : registeredListeners) {
            Log.d(TAG, "onStateChanged: Numero listener: " + registeredListeners.size());
            listener.onStateChanged(gameManagerState, gameManagerState1);
        }
    }

    @Override
    public void onGameMessageReceived(String s, JSONObject jsonObject) {
        Log.d(TAG, "onGameMessageReceived: EventManager ha ricevuto ongamemessage. Numero listener: " + registeredListeners.size());
        for (GameManagerClient.Listener listener : registeredListeners) {
            Log.d(TAG, "onGameMessageReceived: ");
            listener.onGameMessageReceived(s, jsonObject);
        }
    }

}