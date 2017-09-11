package com.example.mirko.partycast.customviews;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Creato da Mirko Pani e Nicola Gilberti per il progetto del corso LPSMT 2017
 */

/*
Custom TextView per avere un font diverso
 */
public class CustomFontTextView extends AppCompatTextView {

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }

}
