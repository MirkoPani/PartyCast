package com.prova.gyrotest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class MainActivity extends AppCompatActivity  implements SensorEventListener{
    //default value for the acceptance ranges
    static float maxAccepted=9.815f;
    static float minAccepted=9.765f;
    SensorManager sm;
    TextView tv;
    int flag=0;

    float axisXval=-1;
    float lastXval=-1;
    float axisYval=-1;
    float lastYval=-1;
    float axisZval=-1;
    float lastZval=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        tv = (TextView) findViewById(R.id.testo);
    }

    @Override
    protected void onResume() {
        sm.registerListener(this,sm.getDefaultSensor(Sensor.TYPE_GRAVITY),SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected void onStop() {
        sm.unregisterListener(this);
        super.onStop();
    }

    //per la gestione dei sensori
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(flag==0){
            axisXval  = abs(event.values[0]);
            axisYval = abs(event.values[1]);
            axisZval = abs(event.values[2]);
            lastXval = axisXval;
            lastYval = axisYval;
            lastZval = axisZval;
            ++flag;
        }
        else{
            lastXval = axisXval;
            lastYval = axisYval;
            lastZval = axisZval;
            axisXval = abs(event.values[0]);
            axisYval = abs(event.values[1]);
            axisZval = abs(event.values[2]);
        }
        if(axisXval >= minAccepted && axisXval <= maxAccepted){
            tv.setText("Device in landscape mode");
        }
        else if(axisYval >= minAccepted && axisYval <= maxAccepted){
            tv.setText("Device in portrait mode");
        }
        else if(axisZval >= minAccepted && axisZval <= maxAccepted){
            tv.setText("Device outstretched");
        }
        else{
            Double gravity = Math.sqrt(pow(axisXval,2)+pow(axisYval,2)+pow(axisZval,2));
            Log.d("Data taken:","x "+axisXval+" y "+axisYval+" z "+axisZval);
            Log.d("Data taken before :","x "+lastXval+" y "+lastYval+" z "+lastZval);
            String s="";
            tv.setText(s);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
