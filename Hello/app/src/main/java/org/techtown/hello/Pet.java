package org.techtown.hello;

import java.io.Serializable;

public class Pet implements Serializable {
    int idPet;
    String pName;

    public int getIdPet() {
        return idPet;
    }

    public void setIdPet(int idPet) {
        this.idPet = idPet;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpSpecies() {
        return pSpecies;
    }

    public void setpSpecies(String pSpecies) {
        this.pSpecies = pSpecies;
    }

    public String getpBreed() {
        return pBreed;
    }

    public void setpBreed(String pBreed) {
        this.pBreed = pBreed;
    }

    public String getpAge() {
        return pAge;
    }

    public void setpAge(String pAge) {
        this.pAge = pAge;
    }

    String pSpecies;
    String pBreed;
    String pAge;
}
