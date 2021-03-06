package com.example.mirko.partycast;

import android.util.Log;

import com.example.mirko.partycast.event.Event;
import com.example.mirko.partycast.event.HostChangedEvent;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * Creato da Mirko Pani e Nicola Gilberti per il progetto del corso LPSMT 2017
 */

public class PartyCastModel {

    private boolean isHost;
    private List<Listener> listeners = new ArrayList<Listener>();


    public PartyCastModel() {
        this.isHost = false;
    }

    public void setHost(boolean value) {
        Log.d(TAG, "setHost:");
        this.isHost = value;

        // Notify everybody that may be interested.
        for (Listener hl : listeners) {
            Log.d(TAG, "setHost: Notifichiamo listener "+hl);
            hl.listen(new HostChangedEvent());
        }
    }
    public boolean getIsHost() {
        return isHost;
    }

    public void addListener(Listener toAdd) {
        listeners.add(toAdd);
    }

    public void removeListener(Listener toRemove) {
        listeners.remove(toRemove);
        Log.d(TAG, "onDestroy: Rimosso listener " + toRemove.toString());
    }
}

interface Listener {
    void listen(Event event);
}