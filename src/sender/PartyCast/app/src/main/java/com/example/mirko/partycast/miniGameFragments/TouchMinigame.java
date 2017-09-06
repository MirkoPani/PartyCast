package com.example.mirko.partycast.miniGameFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.mirko.partycast.PartyCastApplication;
import com.example.mirko.partycast.R;
import com.example.mirko.partycast.customviews.CustomFontTextView;
import com.example.mirko.partycast.customviews.DrawView;
import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.cast.games.GameManagerState;
import com.google.android.gms.cast.games.PlayerInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.google.android.gms.internal.zzt.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class TouchMinigame extends Fragment implements DrawView.DrawViewListener, AdapterView.OnItemSelectedListener {
    private static final String MESSAGE_GRID = "grid";
    private static final String MESSAGE_COLOR = "color";
    private static final String MESSAGE_CLEAR = "clear";
    private static final String MESSAGE_TURN = "turn";
    private static final String MESSAGE_WORDS = "words";
    private static final String MESSAGE_INDEX = "index";
    private static final String MESSAGE_ARTIST = "artist";
    private static final String MESSAGE_GUESS = "guess";
    private static final String MESSAGE_TURNINC = "turninc";



    private View artistUI;
    private View indovinoUI;
    private DrawView drawView;
    private Button btnClear;
    private CustomFontTextView drawInstruction, chooseInstruction;
    private Spinner spinner;
    private CustomFontTextView wordToDraw;
    private Button btnChoose1, btnChoose2, btnChoose3, btnChoose4, btnChoose5, btnChoose6;

    // Tuttte le parole
    private String[] tutteWords;
    private List<String> turnoWords;

    //Numero di match
    int matchNumber;

    //l'indice del nostro turno
    int nostroIndTurno;

    //L'indice della parola corretta
    int indParolaCorretta;

    public TouchMinigame() {
        // Required empty public constructor
    }

    //Hashmap per i valori delle parole
    HashMap<String, Integer> map;

    private GameManagerClient.Listener mListener = new TouchMinigameListener();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.touch_mini_game, container, false);


        artistUI = view.findViewById(R.id.artistUI);
        indovinoUI = view.findViewById(R.id.indovinoUI);
        drawInstruction = (CustomFontTextView) view.findViewById(R.id.drawInstruction);


        //Draview
        drawView = ((DrawView) view.findViewById(R.id.drawView));
        drawView.setListener(this);

        //Spinner
        spinner = (Spinner) view.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.colorArray, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Bottone clear
        btnClear = (Button) view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearGrid();
            }
        });

        //Lista parole
        tutteWords = getResources().getString(R.string.words).split("\\s*,\\s*");
        turnoWords = Arrays.asList(tutteWords);

        //txt della parola da disegnare
        wordToDraw = (CustomFontTextView) view.findViewById(R.id.wordToDraw);

        //bottoni  per la stampa delle scelte per indovinare il disegno
        btnChoose1 = (Button) view.findViewById(R.id.btnChoose1);
        btnChoose2 = (Button) view.findViewById(R.id.btnChoose2);
        btnChoose3 = (Button) view.findViewById(R.id.btnChoose3);
        btnChoose4 = (Button) view.findViewById(R.id.btnChoose4);
        btnChoose5 = (Button) view.findViewById(R.id.btnChoose5);
        btnChoose6 = (Button) view.findViewById(R.id.btnChoose6);
        //assegnamento dell'OnClickListener ai bottoni
        btnChoose1.setOnClickListener(new sendGuessMessageListener());
        btnChoose2.setOnClickListener(new sendGuessMessageListener());
        btnChoose3.setOnClickListener(new sendGuessMessageListener());
        btnChoose4.setOnClickListener(new sendGuessMessageListener());
        btnChoose5.setOnClickListener(new sendGuessMessageListener());
        btnChoose6.setOnClickListener(new sendGuessMessageListener());
        //textfield iniziale per chi deve indovinare
        chooseInstruction = (CustomFontTextView) view.findViewById(R.id.chooseInstruction);

        //Inizializzazione map
        map = new HashMap<String, Integer>();

        disableIndoviniBtn();
        drawView.setTouchEnabled(false);
        drawView.setEnabled(false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        startMatch();
    }

    //Iniziamo il match
    private void startMatch() {
        matchNumber = 0;
        updateTurnIndices();

        if (isMyTurn()) {
            // Peschiamo 6 parole a caso
            turnoWords = getRandomWords(6);
            indParolaCorretta = (new Random()).nextInt(turnoWords.size());

            // Mandiamo agli altri le parole che abbiamo pescato tramite il messaggio del turno
            sendTurnMessage();
        }
        beginTurn();
    }

    //Inizia il turno capendo se siamo artisti o indovini
    private void beginTurn() {
        Log.d(TAG, "beginTurn: ");
        //Puliamo hasmap dai valori di prima

        map.clear();
        if (isMyTurn()) {
            //Siamo artisti
            beginArtistTurn();
        } else {
            beginIndoviniTurn();
        }
    }

    //Inizia il turno di chi deve indovinare cosa sta disegnando.
    private void beginIndoviniTurn() {
        clearGrid();
        setIndoviniUI();
    }


    private void beginArtistTurn() {
        //Puliamo griglia
        clearGrid();
        //Informiamo chi è l'artista
        sendArtistMessage();
        //Settiamo la ui per l'artista
        setArtistUI();
    }

    //Mostra la UI per chi deve indovinare
    private void setIndoviniUI() {
        Log.d(TAG, "setIndoviniUI: ");
        artistUI.setVisibility(View.GONE);
        btnClear.setVisibility(View.GONE);
        //drawView.setTouchEnabled(false);
        drawView.setVisibility(View.GONE);
        indovinoUI.setVisibility(View.VISIBLE);
        wordToDraw.setVisibility(View.GONE);
        btnChoose1.setText(turnoWords.get(0));
        btnChoose2.setText(turnoWords.get(1));
        btnChoose3.setText(turnoWords.get(2));
        btnChoose4.setText(turnoWords.get(3));
        btnChoose5.setText(turnoWords.get(4));
        btnChoose6.setText(turnoWords.get(5));
        map.put(turnoWords.get(0), 0);
        map.put(turnoWords.get(1), 1);
        map.put(turnoWords.get(2), 2);
        map.put(turnoWords.get(3), 3);
        map.put(turnoWords.get(4), 4);
        map.put(turnoWords.get(5), 5);

        btnChoose1.setVisibility(View.VISIBLE);
        btnChoose2.setVisibility(View.VISIBLE);
        btnChoose3.setVisibility(View.VISIBLE);
        btnChoose4.setVisibility(View.VISIBLE);
        btnChoose5.setVisibility(View.VISIBLE);
        btnChoose6.setVisibility(View.VISIBLE);
        chooseInstruction.setVisibility(View.VISIBLE);

       // enableIndoviniBtn();

    }

    //Mostra la UI per l'artista
    private void setArtistUI() {
        Log.d(TAG, "setArtistUI: ");
        artistUI.setVisibility(View.VISIBLE);
        //drawView.setTouchEnabled(true);
        drawView.setVisibility(View.VISIBLE);
        drawInstruction.setVisibility(View.VISIBLE);
        btnClear.setVisibility(View.VISIBLE);
        btnClear.setEnabled(true);
        spinner.setVisibility(View.VISIBLE);
        Log.d(TAG, "setArtistUI: ");
        Log.d(TAG, "setArtistUI: indParolaCorretta: "+indParolaCorretta);
        Log.d(TAG, "setArtistUI: wordtodraw.settext: "+turnoWords.get(indParolaCorretta));
        wordToDraw.setVisibility(View.VISIBLE);
        wordToDraw.setText(turnoWords.get(indParolaCorretta));
        btnChoose1.setVisibility(View.GONE);
        btnChoose2.setVisibility(View.GONE);
        btnChoose3.setVisibility(View.GONE);
        btnChoose4.setVisibility(View.GONE);
        btnChoose5.setVisibility(View.GONE);
        btnChoose6.setVisibility(View.GONE);
        chooseInstruction.setVisibility(View.GONE);
        drawView.clear();
    }

    //Manda un messaggio per avvisare che siamo l'artista
    private void sendArtistMessage() {
        Log.d(TAG, "sendArtistMessage: ");
        JSONObject jsonMessage = new JSONObject();
        GameManagerClient gameManagerClient = PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient();
        if (PartyCastApplication.getInstance().getCastConnectionManager().isConnectedToReceiver()) {
            jsonMessage = new JSONObject();
            try {
                jsonMessage.put(MESSAGE_ARTIST, gameManagerClient.getLastUsedPlayerId());
            } catch (JSONException e) {
                Log.e(TAG, "Error creating JSON message", e);
                return;
            }
            PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient().sendGameMessage(jsonMessage);
        }
    }

    //Manda un messaggio per iniziare il turno: contiene il turno, le parole e l'indice di quella esatta
    private void sendTurnMessage() {
        JSONObject jsonMessage = new JSONObject();
        try {
            jsonMessage.put(MESSAGE_TURN, matchNumber);
            jsonMessage.put(MESSAGE_WORDS, TextUtils.join(",", turnoWords.toArray()));
            jsonMessage.put(MESSAGE_INDEX, indParolaCorretta);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON message", e);
            return;
        }
        Log.d(TAG, "sendTurnMessage: inviato messaggio turno" + jsonMessage.toString());
        PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient().sendGameMessage(jsonMessage);
    }


    //Ritorna una lista con le parole prese a caso da tutte
    private List<String> getRandomWords(int numWords) {
        List<String> result = new ArrayList<>();

        Collections.addAll(result, tutteWords);
        Collections.shuffle(result);
        result = result.subList(0, numWords);

        return result;
    }

    //Restituisce se è il nostro turno per disegnare o meno.
    private boolean isMyTurn() {
        GameManagerClient gameManagerClient = PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient();
        if (PartyCastApplication.getInstance().getCastConnectionManager().isConnectedToReceiver()) {
            GameManagerState state = gameManagerClient.getCurrentState();
            int numParticipants = state.getPlayersInState(
                    GameManagerClient.PLAYER_STATE_PLAYING).size();
            if (numParticipants <= 1) {
                Log.w(TAG, "isMyTurn: no participants - default to true.");
                return true;
            }
            int participantTurnIndex = matchNumber % numParticipants;

            Log.d(TAG, String.format("isMyTurn: %d participants, turn #%d, my turn is #%d",
                    numParticipants, matchNumber, nostroIndTurno));
            return (nostroIndTurno == participantTurnIndex);
        }
        return true;
    }

    //Dobbiamo ottenere l'indice del nostro turno. Per farlo iteriamo tra gli id dei player connessi
    private void updateTurnIndices() {
        GameManagerClient gameManagerClient = PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient();
        if (PartyCastApplication.getInstance().getCastConnectionManager().isConnectedToReceiver()) {
            GameManagerState state = gameManagerClient.getCurrentState();
            ArrayList<String> ids = new ArrayList<>();
            for (PlayerInfo playerInfo : state.getPlayersInState(
                    GameManagerClient.PLAYER_STATE_PLAYING)) {
                ids.add(playerInfo.getPlayerId());
            }
            Collections.sort(ids);
            // Il nostro turno e'
            nostroIndTurno = ids.indexOf(gameManagerClient.getLastUsedPlayerId());
        }

    }


    //Puliamo la griglia sia sender che receiver
    private void clearGrid() {
        drawView.clear();
        //Mandiamo messaggio pulizia
        JSONObject jsonMessage = new JSONObject();
        try {
            jsonMessage.put(MESSAGE_CLEAR, 1);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON message", e);
            return;
        }
        PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient().sendGameMessage(jsonMessage);

    }

    //funzione che serve per bloccare i bottoni
    private void disableIndoviniBtn() {
        btnChoose1.setEnabled(false);
        btnChoose2.setEnabled(false);
        btnChoose3.setEnabled(false);
        btnChoose4.setEnabled(false);
        btnChoose5.setEnabled(false);
        btnChoose6.setEnabled(false);
    }
    //funzione che serve per bloccare i bottoni
    private void enableIndoviniBtn() {
        btnChoose1.setEnabled(true);
        btnChoose2.setEnabled(true);
        btnChoose3.setEnabled(true);
        btnChoose4.setEnabled(true);
        btnChoose5.setEnabled(true);
        btnChoose6.setEnabled(true);
    }


    @Override
    //Chiamato dal listener quando viene toccato un quadrato
    public void onDrawEvent(int gridX, int gridY, short colorIndex) {
        JSONObject jsonMessage = new JSONObject();
        try {
            jsonMessage.put(MESSAGE_GRID, (gridX + 1) + gridY * DrawView.GRID_SIZE);
            int color = drawView.getmSelectedColor();
            String colorString = "";
            switch (color) {
                case 1:
                    colorString = "blue";
                    break;
                case 2:
                    colorString = "red";
                    break;
                case 3:
                    colorString = "green";
                    break;
                default:
                    colorString = "blue";
                    break;
            }
            jsonMessage.put(MESSAGE_COLOR, colorString);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON message", e);
            return;
        }
        PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient().sendGameMessage(jsonMessage);
        Log.d(TAG, "onDrawEvent: " + jsonMessage.toString());
    }


    //Quando selezioniamo un colore
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        drawView.setmSelectedColor((short) ++position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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


    private class TouchMinigameListener implements GameManagerClient.Listener {

        @Override
        public void onStateChanged(GameManagerState current, GameManagerState old) {

            //Se stiamo mostrando info a schermo blocchiamo gameplay
            if(current.getGameplayState()==GameManagerClient.GAMEPLAY_STATE_SHOWING_INFO_SCREEN)
            {
                //Unregister shake
                Log.d(TAG, "onStateChanged: info screen");
              disableIndoviniBtn();
                drawView.setTouchEnabled(false);
            }
            if(current.getGameplayState()==GameManagerClient.GAMEPLAY_STATE_RUNNING)
            {

                    drawView.setTouchEnabled(true);
                    drawView.setEnabled(true);
                    enableIndoviniBtn();
            }

        }

        @Override
        public void onGameMessageReceived(String playerId, JSONObject message) {
            Log.d(TAG, "onGameMessageReceived: " + message);
            if (message.has(MESSAGE_WORDS)) {
                try {
                    Log.d(TAG, "onGameMessageReceived: ricevuto message_words");
                    matchNumber = message.getInt(MESSAGE_TURN);
                    turnoWords = Arrays.asList(message.getString(MESSAGE_WORDS).split("\\s*,\\s*"));
                    indParolaCorretta=message.getInt(MESSAGE_INDEX);
                    Log.d(TAG, "onGameMessageReceived:" + turnoWords.toString());
                    beginTurn();
                } catch (JSONException e) {
                    Log.e(TAG, "onGameMessageReceived", e);
                }
            }
            if (message.has(MESSAGE_TURNINC)) {
                //Cambiamo turno
                Log.d(TAG, "onGameMessageReceived: nel messaggio ce message_turninc");
                if (isMyTurn())
                    onDoneClicked();

                updateViewVisibility();
            }
        }
    }


    private void sendGuessMessage(int i) {
        JSONObject jsonMessage = new JSONObject();
        try {
            jsonMessage.put(MESSAGE_GUESS, i);

        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON message", e);
            return;
        }
        Log.d(TAG, "sendTurnMessage: inviato messaggio guess" + jsonMessage.toString());
        PartyCastApplication.getInstance().getCastConnectionManager().getGameManagerClient().sendGameMessage(jsonMessage);
    }

    private void onDoneClicked() {
        // Increment turn number
        matchNumber = matchNumber + 1;

        // Choose random word subset and correct word
        turnoWords = getRandomWords(6);
        indParolaCorretta = (new Random()).nextInt(turnoWords.size());

        // Send new turn data to others
        sendTurnMessage();

        beginTurn();
        updateViewVisibility();
    }

    private void updateViewVisibility() {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Set UI for either artist or guesser based on turn
        if (isMyTurn()) {
            setArtistUI();
        } else {
            setIndoviniUI();
        }
    }

    private void setTextChooseInstruction() {
        chooseInstruction.setText("In attesa degli altri");
    }

    private class sendGuessMessageListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //cose belle
            sendGuessMessage(map.get(((Button) v).getText()));

            setTextChooseInstruction();
            disableIndoviniBtn();
        }
    }
}