package com.example.mirko.partycast.messages;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Creato da Mirko Pani e Nicola Gilberti per il progetto del corso LPSMT 2017
 */
/*
Classe modello per il messaggio ready
*/
public class PlayerReadyMessage extends Message {
    private static final String TAG = "Player Ready Message";
    private int avatar;
    private String name;

    public PlayerReadyMessage(int avatar,String name){
        this.avatar = avatar;
        this.name = name;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jo = new JSONObject();
        try{
            jo.put("Avatar",avatar);
            jo.put("Name",name);
        }catch(JSONException e){
            Log.e(TAG,"Json Exception, not able to create the message!");
        }
        return jo;
    }
}
