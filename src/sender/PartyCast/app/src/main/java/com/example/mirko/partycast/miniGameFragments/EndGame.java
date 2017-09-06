package com.example.mirko.partycast.miniGameFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mirko.partycast.PartyCastApplication;
import com.example.mirko.partycast.R;
import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.cast.games.GameManagerState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A simple {@link Fragment} subclass.
 */
public class EndGame extends Fragment {
    private static final String TAG = "EndGame";
    private GameManagerClient.Listener mListener = new EndgameListener();
    private ArrayList<String> listaPlayer;
    private Map classifica,avatar;
    private LinearLayout ll;
    private ArrayList<View> lista;


    public EndGame() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_end_game, container, false);

        //hashmap per la classifica
        listaPlayer = new ArrayList<String>();
        classifica = new HashMap<String,Integer>();
        //la ordino in maniera decrescente
        //classifica = sortByComparator(classifica,false);
        //hashmap per l'associazione player-avatar
        avatar = new HashMap<String,Integer>();
        //layout in cui andrà la classifica
        ll = (LinearLayout) view.findViewById(R.id.ll);
        lista = new ArrayList<View>();
        
        return view;
    }

    @Override
    public void onResume() {
        if (PartyCastApplication.getInstance().getCastConnectionManager().isConnectedToReceiver()) {
            PartyCastApplication.getInstance().addListenerToEventManager(mListener);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient() != null) {
            PartyCastApplication.getInstance().removeListenerToEventManager(mListener);
        }
        super.onPause();
    }

    private class EndgameListener implements GameManagerClient.Listener {

        @Override
        public void onStateChanged(GameManagerState gameManagerState, GameManagerState gameManagerState1) {
            Log.d(TAG, "onStateChanged: ");
        }

        @Override
        public void onGameMessageReceived(String s, JSONObject jsonObject) {
            JSONArray j;

            Log.d(TAG, "onGameMessageReceived: ");
            if(jsonObject.has("classifica")){
                try {
                    j = jsonObject.getJSONArray("classifica");
                    for(int i=0;i< j.length();i++){
                        String playerName = j.getJSONObject(i).getString("name");
                        int points = j.getJSONObject(i).getInt("points");
                        int image = j.getJSONObject(i).getInt("avatar");
                        listaPlayer.add(playerName);
                        classifica.put(playerName,points);
                        avatar.put(playerName,image);
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "JSON error!");
                }
                stampaClassifica();
            }
        }
    }

    private void stampaClassifica() {

        /*for(int i=0;i<classifica.size();i++){
           // lista.add(getLayoutInflater(savedInstanceState).inflate(R.layout.elemento_classifica,ll,false));
        }
        for(int i=0;i<classifica.size();i++){
            ViewGroup.LayoutParams lp = ((ImageView)lista.get(i).findViewById(R.id.playerAvatar)).getLayoutParams();
            lp.width = 50;
            lp.height = 50;
            (lista.get(i).findViewById(R.id.playerAvatar)).setLayoutParams(lp);
            ((TextView)lista.get(i).findViewById(R.id.playerName)).setText(listaPlayer.get(i));
            ((TextView)lista.get(i).findViewById(R.id.playerName)).setText(""+classifica.get(listaPlayer.get(i)));
            ((ImageView)lista.get(i).findViewById(R.id.playerAvatar)).setImageResource(Avatar.getAvatarDrowableFromInt((Integer)avatar.get(listaPlayer.get(i))));
        }*/
    }


    //order true -> ascendent false ->descending
    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap,final boolean order)
    {

        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1,
                               Entry<String, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });
        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
