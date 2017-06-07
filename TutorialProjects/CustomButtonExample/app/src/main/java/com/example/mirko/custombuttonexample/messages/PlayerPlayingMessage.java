package com.example.mirko.custombuttonexample.messages;

import org.json.JSONObject;

/**
 * Created by MirkoPortatile on 07/06/2017.
 */

public class PlayerPlayingMessage extends Message{

    public PlayerPlayingMessage()
    {

    }

    @Override
    public JSONObject toJSON() {
        JSONObject serialized = new JSONObject();
        return serialized;
    }

}
