package com.example.mirko.partycast.messages;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Creato da Mirko Pani e Nicola Gilberti per il progetto del corso LPSMT 2017
 */
/*
Classe modello per il messaggio di inizio gioco
 */
public class PlayerPlayingMessage extends Message{
    int nSfide;

    public PlayerPlayingMessage(int n)
    {
        this.nSfide=n;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject serialized = new JSONObject();
        try{
            serialized.put("numMiniHostChoose",nSfide);
        }catch(JSONException e){
            Log.e(TAG,"Json Exception, not able to create the message!");
        }
        return serialized;
    }

}
