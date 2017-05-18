package com.example.mirko.custombuttonexample;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mirko.custombuttonexample.customviews.MyBounceInterpolator;
import com.example.mirko.custombuttonexample.customviews.TypeWriter;
import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer{

    private CastConnectionManager connectionManager;
    private static final String TAG = "MainActivity";
    private MenuItem mItem = null;
    private MediaRouteButton mediaRouteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPlay = (Button)findViewById(R.id.buttonPlay);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bouncebutton);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 30);
        myAnim.setInterpolator(interpolator);

        btnPlay.startAnimation(myAnim);

        //animazione avatar
        ImageView imgAvatar=(ImageView) findViewById(R.id.imgAvatar);
        imgAvatar.setBackgroundResource(R.drawable.avatartitlescreen);

        AnimationDrawable avatarAnimation = (AnimationDrawable) imgAvatar.getBackground();
        avatarAnimation.start();


        //TypeWriter: animazione testo avatar
        TypeWriter writer = (TypeWriter)findViewById(R.id.typewriter);
       // writer.setTypeface(Typeface.createFromAsset(getAssets(), "berlin.ttf"));
        //Add a character every 150ms
        writer.setCharacterDelay(150);
        writer.animateText("Ciao Gilbo!");

        connectionManager=PartyCastApplication.getInstance().getCastConnectionManager();
        mediaRouteButton = (MediaRouteButton) findViewById(R.id.media_route_button);
        mediaRouteButton.setRouteSelector(connectionManager.getMediaRouteSelector());

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaRouteButton.performClick();
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
    }

    protected void onPause() {
        CastConnectionManager manager =
                PartyCastApplication.getInstance().getCastConnectionManager();
        manager.stopScan();
        manager.deleteObserver(this);
        super.onPause();
    }

    //Chiamato quando viene scelto il cast
    @Override
    public void update(Observable o, Object arg) {
        Log.d(TAG, "update: test");

        GameManagerClient gameManagerClient= connectionManager.getGameManagerClient();

        if(connectionManager.isConnectedToReceiver() && gameManagerClient.getCurrentState().getConnectedControllablePlayers().size()==0)
        {
            Log.d(TAG, "update: siamo entrati e ora inviamo sendplayerequest");
            //Notifichiamo il receiver che un giocatore è disponibile e salviamo il risultato

            PendingResult<GameManagerClient.GameManagerResult> result= gameManagerClient.sendPlayerAvailableRequest(null);

            result.setResultCallback(new ResultCallback<GameManagerClient.GameManagerResult>() {
                @Override
                public void onResult(@NonNull GameManagerClient.GameManagerResult gameManagerResult) {
                    if(gameManagerResult.getStatus().isSuccess()==false)
                    {
                        PartyCastApplication.getInstance().getCastConnectionManager().disconnectFromReceiver(false);
                        Log.d(TAG, "onResult: Errore su result dopo sendPlayerAvaiable");
                    }
                    else
                    {
                        Log.d(TAG, "onResult: Creiamo la nuova activity!");
                        Intent intent=new Intent(getBaseContext(),GameActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }

    }
}
