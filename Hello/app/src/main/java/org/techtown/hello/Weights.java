package org.techtown.hello;

import java.util.Date;

public class Weights {
    public int getIdWeights() {
        return idWeights;
    }

    public void setIdWeights(int idWeights) {
        this.idWeights = idWeights;
    }

    public int getIdPet() {
        return idPet;
    }

    public void setIdPet(int idPet) {
        this.idPet = idPet;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    private int idWeights;
    private int idPet;
    private Date date;
    private float weight;
}


