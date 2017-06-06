package com.example.mirko.custombuttonexample;

import android.app.Application;

/**
 * Created by MirkoPortatile on 16/05/2017.
 */

//Application che viene eseguita all'avvio del gioco. Contiene gli oggetti generali che saranno usati
public class PartyCastApplication extends Application implements CastConnectionManager.CastAppIdProvider {

    public static PartyCastApplication instance;
    private CastConnectionManager castConnectionManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        castConnectionManager=new CastConnectionManager(this,this);
    }
    public static PartyCastApplication getInstance() {
        return instance;
    }

    public CastConnectionManager getCastConnectionManager() {
        return castConnectionManager;
    }

    @Override
    public String getCastAppId() {
        return getResources().getString(R.string.app_id);
    }


}
