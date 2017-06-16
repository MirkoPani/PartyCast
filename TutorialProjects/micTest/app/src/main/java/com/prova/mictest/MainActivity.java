package com.prova.mictest;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List data;
    int bufferSize;
    AudioRecord audio;
    short[] buff;
    Thread recThread;
    Button btn;
    int state=0;
    Double meanAmpl=0.0;
    int counter=0;
    Double eps = 2.32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int sampleRate=44100;
        try{
            bufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_8BIT);
            Log.d("BOH","Qua ci arriva!");
            audio = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, sampleRate,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_8BIT,bufferSize);
            Log.d("BOH","Qua ci arriva pt.2!");
        }catch (Exception ex){
            Log.d("BOH","Eccezione per qualcosa");
        }
        //
        data = new ArrayList<Double>();
        buff = new short[bufferSize];
        recThread = new Thread(new Runnable() {
            int rec=0;
            @Override
            public void run() {
                while (true) {
                    int res = audio.read(buff, 0, bufferSize);
                    int ampl = ((buff[0] & 0xff)<<8|buff[1]);
                    Double amplitude = 20 * Math.log10(((double)Math.abs(ampl))/32768);
                    //Log.d("StampaDati","reading: " + amplitude);
                    if(state==1) {
                        data.add(amplitude);
                    }
                    if(state==3){
                        if(amplitude<(meanAmpl-eps)){
                            //Log.d("TryngToBlow","possible blow!");
                            //++counter;
                            rec=1;
                        }
                        else{
                            if(rec==1){
                                rec=0;
                                ++counter;
                                Log.d("TryngToBlow","have you finished to blow?");
                            }
                        }
                    }
                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        Log.d("SLEEPTHREAD", "non dorme o dorme troppo");
                    }
                }
            }
        }, "AudioRecorder thread");
        //
        btn = (Button)findViewById(R.id.bottone);
        btn.setText("Sample!");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state==0){
                    state=1;
                    audio.startRecording();
                    if(!recThread.isAlive()){
                        recThread.start();
                    }
                    btn.setText("Stop sampling!");
                }
                else if(state==1){
                    recThread.interrupt();
                    audio.stop();
                    for(int i=0;i<data.size();i++){
                        meanAmpl+=(Double) data.get(i);
                    }
                    meanAmpl/=data.size();
                    Log.d("Mean","meanFromSampling: "+meanAmpl);
                    btn.setText("Can you try to blow on a mic?");
                    state=2;
                    data.clear();
                }
                else if(state==2){
                    state=3;
                    audio.startRecording();
                    //recThread.start();
                    btn.setText("Stop trying!");
                }
                else{
                    Log.d("Test","Number of Blow reached: "+counter);
                    state=0;
                    btn.setText("Sample again?");
                    counter=0;
                    meanAmpl=0.0;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*((TextView)findViewById(R.id.text)).setText("Source: "+audio.getAudioSource()+
                "\nFormat: "+audio.getAudioFormat()+
                "\nChannel config"+audio.getChannelConfiguration());*/

        /*int val = audio.read(buff,0,bufferSize);
        ((TextView)findViewById(R.id.text)).setText("val: "+val);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        audio.stop();
        //recThread.stop();
    }
}