package com.example.mirko.custombuttonexample.customviews;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by MirkoPortatile on 15/05/2017.
 */

    public class FontCache {

        private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

        public static Typeface get(String name, Context context) {
            Typeface tf = fontCache.get(name);
            if(tf == null) {
                try {
                    tf = Typeface.createFromAsset(context.getAssets(), name);
                }
                catch (Exception e) {
                    return null;
                }
                fontCache.put(name, tf);
            }
            return tf;
        }
    }