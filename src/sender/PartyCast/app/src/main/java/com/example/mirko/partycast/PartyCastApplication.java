package com.example.mirko.partycast;

import android.app.Application;

import com.google.android.gms.cast.games.GameManagerClient;

/**
 * Created by MirkoPortatile on 16/05/2017.
 */

//Application che viene eseguita all'avvio del gioco. Contiene gli oggetti generali che saranno usati
public class PartyCastApplication extends Application implements CastConnectionManager.CastAppIdProvider {

    public static PartyCastApplication instance;
    private CastConnectionManager castConnectionManager;
    private PartyCastModel model;
    private EventManager eventManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        castConnectionManager=new CastConnectionManager(this,this);
        model=new PartyCastModel();
        eventManager=new EventManager();

    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void addListenerToEventManager(GameManagerClient.Listener lis)
    {
        this.eventManager.registerListener(lis);
    }
    public void removeListenerToEventManager(GameManagerClient.Listener lis)
    {
        this.eventManager.removeListener(lis);
    }

    public static PartyCastApplication getInstance() {
        return instance;
    }

    public CastConnectionManager getCastConnectionManager() {
        return castConnectionManager;
    }

    public PartyCastModel getModel() {
        return model;
    }


    @Override
    public String getCastAppId() {
        return getResources().getString(R.string.app_id);
    }


}
