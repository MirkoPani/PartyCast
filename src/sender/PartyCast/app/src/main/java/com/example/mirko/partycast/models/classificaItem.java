package com.example.mirko.partycast.models;

/**
 * Created by nicola on 05/09/2017.
 */

public class classificaItem {

    private String name;
    private String points;
    private int avatarImgId;

    public classificaItem(String name,String points,int avatarId){
        this.name=name;
        this.points=points;
        this.avatarImgId =avatarId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public int getAvatarImgId() {
        return avatarImgId;
    }

    public void setAvatarImgId(int avatarImgId) {
        this.avatarImgId = avatarImgId;
    }
}
