package com.example.mirko.partycast.customviews;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Creato da Mirko Pani e Nicola Gilberti per il progetto del corso LPSMT 2017
 */

/*
Custom Button per le schermate iniziali (Per avere un font diverso)
 */
public class ButtonPlus extends AppCompatButton   {

    public ButtonPlus(Context context) {
        super(context);
    }

    public ButtonPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }

    public ButtonPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }
}