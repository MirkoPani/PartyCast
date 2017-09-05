package com.example.mirko.custombuttonexample.miniGameFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mirko.custombuttonexample.PartyCastApplication;
import com.example.mirko.custombuttonexample.R;
import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.cast.games.GameManagerState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static android.os.Build.VERSION_CODES.M;

/**
 * A simple {@link Fragment} subclass.
 */
public class EndGame extends Fragment {
    private static final String TAG = "EndGame";
    private GameManagerClient.Listener mListener = new EndgameListener();
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
        classifica = new HashMap<String,Integer>();
        //la ordino in maniera decrescente
        classifica = sortByComparator(classifica,false);
        //hashmap per l'associazione player-avatar
        avatar = new HashMap<String,Integer>();
        //layout in cui andrà la classifica
        ll = (LinearLayout) view.findViewById(R.id.ll);
        lista = new ArrayList<View>();
        for(int i=0;i<classifica.size();i++){
            lista.add(getLayoutInflater(savedInstanceState).inflate(R.layout.elemento_classifica,ll,false));
        }
        
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
                        String playerName = j.getJSONObject(i).getString("playerName");
                        int points = j.getJSONObject(i).getInt("playerPoints");
                        int image = j.getJSONObject(i).getInt("avatar");
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
        for(int i=0;i<classifica.size();i++){
            ((TextView)lista.get(i).findViewById(R.id.playerName)).setText("cl");
        }
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
