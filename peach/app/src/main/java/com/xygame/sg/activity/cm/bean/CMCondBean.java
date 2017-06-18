package com.xygame.sg.activity.cm.bean;

import java.io.Serializable;

/**
 * Created by moreidols on 16/3/20.
 */
public class CMCondBean implements Serializable {
    private Integer gender;
    private String country;
    private Integer city;

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }
}
