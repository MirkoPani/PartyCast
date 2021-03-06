package com.example.mirko.partycast;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.MediaRouteButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mirko.partycast.customviews.MyBounceInterpolator;
import com.example.mirko.partycast.customviews.TypeWriter;
import com.example.mirko.partycast.viewpages.InstructionFragment;
import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.cast.games.GameManagerState;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;
/*
Prima activity che viene avviata (Schermata iniziale)
 */
public class MainActivity extends GeneralActivity implements Observer {

    private CastConnectionManager connectionManager;
    private static final String TAG = "MainActivity";
    private MenuItem mItem = null;
    private MediaRouteButton mediaRouteButton;
    private GameManagerClient.Listener mListener = new LobbyListener();
    private Button istructionButton;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Impostiamo schermo intero
        setFullScreenListener();

        //Set layout
        setContentView(R.layout.activity_main);

        //Creazione animazione per il pulsante
        Button btnPlay = (Button) findViewById(R.id.buttonPlay);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bouncebutton);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 30);
        myAnim.setInterpolator(interpolator);

        btnPlay.startAnimation(myAnim);


        //animazione avatar
        ImageView imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        imgAvatar.setBackgroundResource(R.drawable.avatartitlescreen);

        AnimationDrawable avatarAnimation = (AnimationDrawable) imgAvatar.getBackground();
        avatarAnimation.start();


        //TypeWriter: animazione testo avatar
        TypeWriter writer = (TypeWriter) findViewById(R.id.typewriter);
        // writer.setTypeface(Typeface.createFromAsset(getAssets(), "berlin.ttf"));
        //Add a character every 150ms
        writer.setCharacterDelay(150);
        writer.animateText("Benvenuti!");

        //Oggetti per il collegamento cast
        connectionManager = PartyCastApplication.getInstance().getCastConnectionManager();
        mediaRouteButton = (MediaRouteButton) findViewById(R.id.media_route_button);
        mediaRouteButton.setRouteSelector(connectionManager.getMediaRouteSelector());

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaRouteButton.performClick();
            }
        });

        //istruction button
        istructionButton = (Button)findViewById(R.id.istruction);
        istructionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(android.R.id.content,new InstructionFragment(),null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        CastConnectionManager manager =
                PartyCastApplication.getInstance().getCastConnectionManager();
        manager.startScan();
        manager.addObserver(this);
        if (manager.isConnectedToReceiver()) {
           // PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient().setListener(mListener);
            PartyCastApplication.getInstance().addListenerToEventManager(mListener);
        }
    }

    protected void onPause() {
        CastConnectionManager manager =
                PartyCastApplication.getInstance().getCastConnectionManager();
        manager.stopScan();
        manager.deleteObserver(this);
        PartyCastApplication.getInstance().removeListenerToEventManager(mListener);
        super.onPause();
    }

    //Chiamato quando viene scelto il cast
    @Override
    public void update(Observable o, Object arg) {
        Log.d(TAG, "update: Siamo entrati in update");

        final GameManagerClient gameManagerClient = connectionManager.getGameManagerClient();

        if (connectionManager.isConnectedToReceiver() && gameManagerClient.getCurrentState().getConnectedControllablePlayers().size() == 0) {
            Log.d(TAG, "update: Players Connected in totale= "+gameManagerClient.getCurrentState().getConnectedPlayers().size() );

            //Settiamo l'eventmanager come listener
            PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient().setListener(PartyCastApplication.getInstance().getEventManager());
            PartyCastApplication.getInstance().addListenerToEventManager(mListener);

            Log.d(TAG, "update: siamo entrati e ora inviamo sendplayerequest");

            //Notifichiamo il receiver che un giocatore è disponibile e salviamo il risultato
            PendingResult<GameManagerClient.GameManagerResult> result = gameManagerClient.sendPlayerAvailableRequest(null);

            result.setResultCallback(new ResultCallback<GameManagerClient.GameManagerResult>() {
                @Override
                public void onResult(@NonNull GameManagerClient.GameManagerResult gameManagerResult) {

                    //Errore
                    if (gameManagerResult.getStatus().isSuccess() == false ) {
                        PartyCastApplication.getInstance().getCastConnectionManager().disconnectFromReceiver(false);
                        Log.d(TAG, "onResult: Errore su result dopo sendPlayerAvaiable");

                        //Lobby chiusa
                    } else if(gameManagerResult.getStatus().isSuccess() && gameManagerClient.getCurrentState().getLobbyState()==2){
                        Log.d(TAG, "onResult: lobby chiusa. Aspettiamo che carichi!");

                        Button btnPlay = (Button) findViewById(R.id.buttonPlay);
                        btnPlay.setEnabled(false);
                        btnPlay.clearAnimation();
                        btnPlay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        btnPlay.setText(". . .");
                    }
                    //Lobby aperta
                    else if(gameManagerResult.getStatus().isSuccess() && gameManagerClient.getCurrentState().getLobbyState()==1){
                        Log.d(TAG, "onResult: lobby era già aperta!.Creiamo nuova activity");

                        //Associamo il nuovo listener

                       // PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient().setListener(PartyCastApplication.getInstance().getEventManager());


                        Intent intent=new Intent(getBaseContext(),GameActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
            });
        }

    }


    private class LobbyListener implements GameManagerClient.Listener {

        @Override
        public void onStateChanged(GameManagerState currentState, GameManagerState previousState) {
            Log.d(TAG, "onStateChanged from " + currentState + " to " + previousState);
            if (currentState.hasLobbyStateChanged(previousState)) {
                Log.d(TAG, "Lobby has changed state: " + currentState);
                Intent intent = new Intent(getBaseContext(), GameActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        public void onGameMessageReceived(String s, JSONObject jsonObject) {
            Log.d(TAG, "Message received: " + jsonObject.toString());
         /*   if(jsonObject.has("host")){
                Log.d(TAG, "onGameMessageReceived: Siamo diventati host!");
                PartyCastApplication.getInstance().getModel().setHost(true);
            }*/
        }

    }

}

