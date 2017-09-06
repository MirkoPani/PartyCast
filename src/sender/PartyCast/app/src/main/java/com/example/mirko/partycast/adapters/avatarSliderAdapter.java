package com.example.mirko.partycast.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mirko.partycast.AvatarFragment;
import com.example.mirko.partycast.R;
import com.example.mirko.partycast.models.Avatar;

import java.util.ArrayList;

/**
 * Created by nicola on 26/05/2017.
 */

public class avatarSliderAdapter extends FragmentPagerAdapter {
    //variabili
    Context context;
    ArrayList<Avatar> avatarList=new ArrayList<Avatar>();

    //costruttore
    public avatarSliderAdapter(FragmentManager fm) {
        super(fm);
        this.context = context;
        avatarList.add(new Avatar("Gino il fattorino", R.drawable.avatar_1,"Veloce come il vento"));
        avatarList.add(new Avatar("Il Lord", R.drawable.avatar_2,"Aristocratico"));
        avatarList.add(new Avatar("Scarlet ", R.drawable.avatar_3,"Bionda bella"));
        avatarList.add(new Avatar("La nerd", R.drawable.avatar_4,"01010101"));
        avatarList.add(new Avatar("La stonza", R.drawable.avatar_5,"Cattivaa"));
        avatarList.add(new Avatar("Cicciogamer89", R.drawable.avatar_6,"Loser"));

    }

    @Override
    public int getCount() {
        return avatarList.size();
    }


    @Override
    public Fragment getItem(int position) {
        AvatarFragment avatarFragment = new AvatarFragment();
        avatarFragment.setAvatar(avatarList.get(position));
        return avatarFragment;
    }


}
