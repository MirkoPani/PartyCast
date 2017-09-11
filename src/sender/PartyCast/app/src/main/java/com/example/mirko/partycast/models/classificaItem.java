package com.example.mirko.partycast.models;

/**
 * Creato da Mirko Pani e Nicola Gilberti per il progetto del corso LPSMT 2017
 */
/*
Classe modello per gli elementi della ListView classifica
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
