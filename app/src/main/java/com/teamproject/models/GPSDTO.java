package com.teamproject.models;

/**
 * Created by 008M on 2016-04-28.
 */
public class GpsDTO {

    private double dlugosc;
    private double szerokosc;
    private String action;
    public double getDlugosc() {
        return dlugosc;
    }

    public void setDlugosc(double dlugosc) {
        this.dlugosc = dlugosc;
    }

    public double getSzerokosc() {
        return szerokosc;
    }

    public void setSzerokosc(double szerokosc) {
        this.szerokosc = szerokosc;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
