package com.example.mirko.custombuttonexample;


import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.mirko.custombuttonexample.miniGameFragments.*;


/**
 * Created by nicola on 08/06/2017.
 */

public class MiniGameManager {
private static String TAG = "MINIGAMEMANAGER";

    public Fragment setMiniGame(String minigameType) {
        switch (minigameType){
            case "touchMinigame":
                return new TouchMinigame();
            case "gyroMinigame":
                return new GyroMinigame();
            case "micMinigame":
                return new MicMinigame();
            case "shakeMinigame":
                return new ShakeMinigame();
            case "endGame":
                return new EndGame();
            default:
                Log.d(TAG,"Non è stato scelto nessun minigioco. errore!");
                return null;
        }
    }
}