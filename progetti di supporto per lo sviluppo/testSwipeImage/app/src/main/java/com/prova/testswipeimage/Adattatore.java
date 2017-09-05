package com.prova.testswipeimage;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import static android.R.attr.padding;

/**
 * Created by nicola on 26/05/2017.
 */

public class Adattatore extends PagerAdapter {
    //variabili
    Context context;
    private int[] gallery = new int[] {
            R.drawable.one,
            R.drawable.two,
            R.drawable.three
    };
    int selected=-1;
    //costruttore
    public Adattatore(Context context){
        this.context=context;
    }

    @Override
    public int getCount() {
        return gallery.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(gallery[position]);
        ((ViewPager) container).addView(imageView, 0);
        //selected=position;
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

}
