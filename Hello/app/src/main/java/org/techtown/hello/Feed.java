package org.techtown.hello;

import java.io.Serializable;

public class Feed implements Serializable, Comparable<Feed> {
    private int idFEED;
    private String FName;
    private String FCompany;
    private String FCountry;
    private int FPrice;
    private String FAge;
    private String FIngredient;
    private String FiRate;
    private String FImg;

    public int getIdFEED() {
        return idFEED;
    }

    public void setIdFEED(int idFEED) {
        this.idFEED = idFEED;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getFCompany() {
        return FCompany;
    }

    public void setFCompany(String FCompany) {
        this.FCompany = FCompany;
    }

    public String getFCountry() {
        return FCountry;
    }

    public void setFCountry(String FCountry) {
        this.FCountry = FCountry;
    }

    public int getFPrice() {
        return FPrice;
    }

    public void setFPrice(int FPrice) {
        this.FPrice = FPrice;
    }

    public String getFAge() {
        return FAge;
    }

    public void setFAge(String FAge) {
        this.FAge = FAge;
    }

    public String getFIngredient() {
        return FIngredient;
    }

    public void setFIngredient(String FIngredient) {
        this.FIngredient = FIngredient;
    }

    public String getFiRate() {
        return FiRate;
    }

    public void setFiRate(String fiRate) {
        FiRate = fiRate;
    }

    public String getFImg() {
        return FImg;
    }

    public void setFImg(String FImg) {
        this.FImg = FImg;
    }

    @Override
    public int compareTo(Feed o) {
        if (this.FPrice< o.getFPrice()){
            return -1;
        }else if (this.FPrice > o.getFPrice()){
            return 1;
        }
        return 0;
    }
}
