package com.prova.sensortest;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.util.List;

import static android.R.attr.type;

/**
 * Created by nicola on 06/06/2017.
 */

public class GestoreSensori extends Activity implements SensorEventListener {
    //attributi
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TextView text;

    public GestoreSensori(SensorManager mSensorManager,TextView t) {
        this.mSensorManager = mSensorManager;
        this.text = t;
    }

    public void printValues(float[] floats){
        String tmp="SensorUnit->it depends on the sensor\n" +
                "Sensore:\n\t"+mSensor.getName()+"\n"+
                "Max range value:(SensorUnit)\n\t"+mSensor.getMaximumRange()+"\n"+
                "Resolution:(SensorUnit)\n\t"+mSensor.getResolution()+"\n"+
                "Range Sampling(delay in μs):\n\t(min)"+mSensor.getMinDelay()+"\n\t(max)"+mSensor.getMaxDelay()+"\n";
        for(int i=0;i<floats.length;i++){
            tmp+=("Dato "+i+": "+floats[i]+"\n");
        }
        text.setText(tmp);
    }

    public void setSensor(int SensorType){
        mSensor = mSensorManager.getDefaultSensor(SensorType);
    }

    public SensorManager getmSensorManager() {
        return mSensorManager;
    }

    public void startListener(){
        mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopListener(){
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //fare cose mentre il sensore invia dati
        printValues(event.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
