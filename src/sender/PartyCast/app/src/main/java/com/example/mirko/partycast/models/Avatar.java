package com.example.mirko.partycast.models;

import com.example.mirko.partycast.R;

/**
 * Created by MirkoPortatile on 30/05/2017.
 */

public class Avatar {
    private String name;

    public String getName() {
        return name;
    }

    public int getImgSrcId() {
        return imgSrcId;
    }

    public String getDescription() {
        return description;
    }

    private int imgSrcId;
    private String description;


    public Avatar(String name, int imgSrc, String description) {
        this.name = name;
        this.imgSrcId = imgSrc;
        this.description = description;
    }
    public static int getAvatarDrowableFromInt(int a){
        switch(a){
            case 1:return R.drawable.avatar_1;
            case 2:return R.drawable.avatar_2;
            case 3:return R.drawable.avatar_3;
            case 4:return R.drawable.avatar_4;
            case 5:return R.drawable.avatar_5;
            case 6:return R.drawable.avatar_6;
            default:return R.drawable.avatar_1;
        }
    }
}
