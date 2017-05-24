package com.example.mirko.custombuttonexample;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.mirko.custombuttonexample.customviews.ButtonPlus;
import com.example.mirko.custombuttonexample.messages.PlayerReadyMessage;
import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.common.api.PendingResult;

import static android.R.attr.onClick;

/**
 * Created by MirkoPortatile on 17/05/2017.
 */

public class LobbyFragment extends Fragment{
    GameManagerClient gmc;
    private static final String TAG = "LobbyFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View flagsView= inflater.inflate(R.layout.lobby_fragment, container, false);
        //Game manager client
        gmc = PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient();

        return flagsView;
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //bottone ready
        ButtonPlus ready = (ButtonPlus) view.findViewById(R.id.ready);
        ready.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                EditText et = (EditText) view.findViewById(R.id.PlayerName);
                String name = et.getText().toString();
                int avatar=1;
                PlayerReadyMessage prm = new PlayerReadyMessage(avatar,name);
                PendingResult<GameManagerClient.GameManagerResult> result= gmc.sendPlayerReadyRequest(prm.toJSON());
                Log.d(TAG,"player "+name+" con avatar "+avatar+" ready!");
            }
        });

    }
}
