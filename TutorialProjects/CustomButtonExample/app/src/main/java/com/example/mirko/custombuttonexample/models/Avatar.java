package com.example.mirko.custombuttonexample.models;

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

}
