package com.example.mirko.custombuttonexample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mirko.custombuttonexample.adapters.avatarSliderAdapter;
import com.example.mirko.custombuttonexample.customviews.ButtonPlus;
import com.example.mirko.custombuttonexample.messages.PlayerReadyMessage;
import com.example.mirko.custombuttonexample.viewpages.DeactivableViewPager;
import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.common.api.PendingResult;

/**
 * Created by MirkoPortatile on 17/05/2017.
 */

public class LobbyFragment extends Fragment {
    GameManagerClient gmc;
    DeactivableViewPager viewPager;
    avatarSliderAdapter adapter;
    ButtonPlus btnReady;
    private static final String TAG = "LobbyFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View flagsView = inflater.inflate(R.layout.lobby_fragment, container, false);
        //Game manager client
        gmc = PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient();

        viewPager = (DeactivableViewPager) flagsView.findViewById(R.id.view_pager);
        adapter = new avatarSliderAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(false, new FadePageTransformer());
        return flagsView;
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //bottone ready
        btnReady = (ButtonPlus) view.findViewById(R.id.ready);
        btnReady.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });

    }

    private void sendReadyMessage(String name) {
        int avatar = viewPager.getCurrentItem() + 1;
        PlayerReadyMessage prm = new PlayerReadyMessage(avatar,name);
        PendingResult<GameManagerClient.GameManagerResult> result= gmc.sendPlayerReadyRequest(prm.toJSON());

        Log.d(TAG, "player " + name + " con avatar " + avatar + " ready!");

        btnReady.setEnabled(false);
        ((ButtonPlus) btnReady).setText("In attesa");
        viewPager.setEnabled(false);
    }

    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getView().getContext());
        View promptView = layoutInflater.inflate(R.layout.prompt_name, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getView().getContext());
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.editName);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG, "onClick: " + editText.getText());
                        sendReadyMessage(editText.getText().toString());
                    }
                })
                .setNegativeButton("Torna indietro",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}

class FadePageTransformer implements ViewPager.PageTransformer {
    public void transformPage(View view, float position) {

        if (position < -1 || position > 1) {
            view.setAlpha(0);
        } else if (position <= 0 || position <= 1) {
            // Calculate alpha. Position is decimal in [-1,0] or [0,1]
            float alpha = (position <= 0) ? position + 1 : 1 - position;
            view.setAlpha(alpha);
        } else if (position == 0) {
            view.setAlpha(1);
        }
    }
}
