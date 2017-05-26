package com.prova.testswipeimage;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //variabili
    TextView tv;
    Button btn;
    ViewPager viewPager;
    Adattatore adapter;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new Adattatore(this);
        viewPager.setAdapter(adapter);
        tv = (TextView)findViewById(R.id.textView);
        btn = (Button) findViewById(R.id.click);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmp =  viewPager.getCurrentItem();
                tv.setText("Hai cliccato"+tmp);
            }
        });

    }
}
