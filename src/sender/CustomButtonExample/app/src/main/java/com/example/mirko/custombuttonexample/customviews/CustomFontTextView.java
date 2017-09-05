package com.example.mirko.custombuttonexample.customviews;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by MirkoPortatile on 13/06/2017.
 */

public class CustomFontTextView extends AppCompatTextView {

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }

}
