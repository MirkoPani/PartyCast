package com.example.mirko.partycast.messages;

import org.json.JSONObject;

/**
 * Created by nicola on 24/05/2017.
 */

public abstract class Message {

    public abstract JSONObject toJSON();

}