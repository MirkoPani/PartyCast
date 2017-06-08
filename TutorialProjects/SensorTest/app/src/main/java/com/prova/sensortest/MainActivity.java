package com.prova.sensortest;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {
    public GestoreSensori gs;
    public List<Sensor> lsens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final TextView textview= (TextView) findViewById(R.id.textView);
        textview.setMovementMethod(new ScrollingMovementMethod());

        gs = new GestoreSensori((SensorManager) getSystemService(SENSOR_SERVICE),textview);

        lsens = gs.getmSensorManager().getSensorList(Sensor.TYPE_ALL);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("nothing");
        for(int i=0;i<lsens.size();i++){
            list.add(lsens.get(i).getStringType());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                gs.stopListener();
                String sensor_type = parent.getItemAtPosition(pos).toString();
                if(!sensor_type.equals("nothing")){
                    int position=-1;
                    for (int i=0;i<lsens.size();i++){
                        if(lsens.get(i).getStringType().equals(sensor_type)){
                            position=i;
                        }
                    }
                    gs.setSensor(lsens.get(position).getType());
                    gs.startListener();
                }
                else{
                    gs.stopListener();
                    textview.setText(R.string.testorandom);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
