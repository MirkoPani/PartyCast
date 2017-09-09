package com.example.mirko.partycast.viewpages;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mirko.partycast.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstructionFragment extends Fragment {
    private Button back;
    private Button b1,b2,b3;
    private View t1,t2,t3;
    private View view;

    public InstructionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_instruction, container, false);
        //bottone per tornare indietro
        back = (Button)view.findViewById(R.id.button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });
        //bottoni per testo
        b1= (Button) view.findViewById(R.id.button1);
        b2= (Button) view.findViewById(R.id.button2);
        b3= (Button) view.findViewById(R.id.button3);
        t1= (View) view.findViewById(R.id.artisti_e_indovini_spiegazioni);
        t2= (View) view.findViewById(R.id.shake_spiegazione);
        t3= (View) view.findViewById(R.id.cerca_e_raggiungi_spiegazione);
        t1.setVisibility(View.GONE);
        t2.setVisibility(View.GONE);
        t3.setVisibility(View.GONE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t1.getVisibility() == View.GONE){
                    t1.setVisibility(View.VISIBLE);
                    b1.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.arrow_up,0);// left, top, right, bottom
                }else{
                    t1.setVisibility(View.GONE);
                    b1.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.arrow_down,0);
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t2.getVisibility() == View.GONE){
                    t2.setVisibility(View.VISIBLE);
                    b2.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.arrow_up,0);// left, top, right, bottom
                }else{
                    t2.setVisibility(View.GONE);
                    b2.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.arrow_down,0);
                }
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(t3.getVisibility() == View.GONE){
                    t3.setVisibility(View.VISIBLE);
                    b3.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.arrow_up,0);// left, top, right, bottom
                }else{
                    t3.setVisibility(View.GONE);
                    b3.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.arrow_down,0);
                }
            }
        });
        return view;
    }

}
