package com.nishiket.weather.Domains;

import androidx.recyclerview.widget.RecyclerView;

public class FutureDomain {
    private String days;
    private String picPath;
    private String statud;
    private int maxTemp;
    private int lowTemp;

    public FutureDomain(String days, String picPath, String statud, int maxTemp, int lowTemp) {
        this.days = days;
        this.picPath = picPath;
        this.statud = statud;
        this.maxTemp = maxTemp;
        this.lowTemp = lowTemp;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getStatud() {
        return statud;
    }

    public void setStatud(String statud) {
        this.statud = statud;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(int lowTemp) {
        this.lowTemp = lowTemp;
    }
}
