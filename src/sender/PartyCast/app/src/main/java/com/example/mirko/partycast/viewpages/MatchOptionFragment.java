package com.example.mirko.partycast.viewpages;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mirko.partycast.CastConnectionManager;
import com.example.mirko.partycast.PartyCastApplication;
import com.example.mirko.partycast.R;
import com.example.mirko.partycast.customviews.ButtonPlus;
import com.example.mirko.partycast.messages.PlayerPlayingMessage;
import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchOptionFragment extends Fragment{
    GameManagerClient gmc;
    RadioGroup rg;
    ButtonPlus bp;

    private static final String TAG = "MatchOptionFragment";


    public MatchOptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_match_option, container, false);

        //Game manager client
        gmc = PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient();

        //vado a recuperare gli elementi del layout
        rg = (RadioGroup)view.findViewById(R.id.radioGroup);
        bp = (ButtonPlus)view.findViewById(R.id.iniziaGioco);
        bp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idRadio = rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton)view.findViewById(idRadio);
                Log.d(TAG, "onClick: numero partite scelto: "+Integer.valueOf(rb.getText().toString()));
                sendPlayingMessage(Integer.valueOf(rb.getText().toString()));
            }
        });
        return view;
    }


    public void sendPlayingMessage(int nSfide){
        PlayerPlayingMessage ppm=new PlayerPlayingMessage(nSfide);

        final CastConnectionManager ccm=PartyCastApplication.getInstance().getCastConnectionManager();
        if(ccm.isConnectedToReceiver())
        {
            PendingResult<GameManagerClient.GameManagerResult> result = gmc.sendPlayerPlayingRequest(ppm.toJSON());

            result.setResultCallback(
                    new ResultCallback<GameManagerClient.GameManagerResult>() {
                        @Override
                        public void onResult(final GameManagerClient.GameManagerResult
                                                     gameManagerResult) {
                            if (gameManagerResult.getStatus().isSuccess()) {
                                Log.d(TAG, "onResult: Playing!");
                            } else {
                                ccm.disconnectFromReceiver(false);
                                Log.d(TAG, "onResult: Errore sendPlayerPlaying");
                            }

                        }
                    });


        }



        Log.d(TAG, "sendPlayingMessage: ");
    }

}
