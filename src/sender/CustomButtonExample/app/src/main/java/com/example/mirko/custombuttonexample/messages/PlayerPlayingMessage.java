package com.example.mirko.custombuttonexample.messages;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.name;
import static android.content.ContentValues.TAG;

/**
 * Created by MirkoPortatile on 07/06/2017.
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
