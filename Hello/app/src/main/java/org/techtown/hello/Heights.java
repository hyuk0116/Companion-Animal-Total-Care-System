package org.techtown.hello;

import java.io.Serializable;
import java.util.Date;

public class Heights implements Serializable {
    int idHeights;
    float pHeights;
    int petID;
    Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIdHeights() {
        return idHeights;
    }

    public void setIdHeights(int idHeights) {
        this.idHeights = idHeights;
    }

    public float getpHeights() {
        return pHeights;
    }

    public void setpHeights(float pHeights) {
        this.pHeights = pHeights;
    }

    public int getPetID() {
        return petID;
    }

    public void setPetID(int petID) {
        this.petID = petID;
    }
}
