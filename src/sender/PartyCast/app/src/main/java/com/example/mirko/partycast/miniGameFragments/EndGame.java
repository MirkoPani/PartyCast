package com.example.mirko.partycast.miniGameFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mirko.partycast.GameActivity;
import com.example.mirko.partycast.PartyCastApplication;
import com.example.mirko.partycast.R;
import com.example.mirko.partycast.adapters.classificaAdapter;
import com.example.mirko.partycast.models.Avatar;
import com.example.mirko.partycast.models.classificaItem;
import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.cast.games.GameManagerState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private ArrayList<classificaItem> listaPlayer;

    private ListView listView;
private classificaAdapter adapter;
    public EndGame() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_end_game, container, false);

        //hashmap per la classifica
        listaPlayer = new ArrayList<classificaItem>();

        //listaPlayer.add(new classificaItem("Ciao",5+"",R.drawable.avatar_1));

        adapter = new classificaAdapter(getActivity(), listaPlayer, R.color.endGameItem);

        listView = (ListView) view.findViewById(R.id.classificaListView);

        listView.setAdapter(adapter);

        //Notifichiamo dell'avvenuto cambio
        ((GameActivity)getActivity()).sendMiniGameChangedConfermation();

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
                Log.d(TAG, "onGameMessageReceived: Classifica ce");
                try {
                    j = jsonObject.getJSONArray("classifica");
                    for(int i=0;i< j.length();i++){

                        String playerName = j.getJSONObject(i).getString("name");
                        Log.d(TAG, "onGameMessageReceived: name: "+playerName);
                        int points = j.getJSONObject(i).getInt("points");
                        int imageId = Avatar.getAvatarDrowableFromInt(j.getJSONObject(i).getInt("avatar")) ;
                        listaPlayer.add(new classificaItem(playerName,points+"",imageId));
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "JSON error!");
                }
                Log.d(TAG, "onGameMessageReceived: dimensione listaplayer "+listaPlayer.size());
                updateAdapter();
            }
        }
    }

    private void updateAdapter() {
        Log.d(TAG, "updateAdapter: ");

        adapter.notifyDataSetChanged();
        listView.invalidateViews();
        listView.refreshDrawableState();
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
