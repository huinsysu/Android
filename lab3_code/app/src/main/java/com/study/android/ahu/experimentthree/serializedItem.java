package com.study.android.ahu.experimentthree;

import java.io.Serializable;

/**
 * Created by ahu on 16-10-15.
 */
public class serializedItem implements Serializable {
    private String name;
    private String phone;
    private String type;
    private String address;
    private String backgroundColor;

    public String getName() {
        return name;
    }
    public String getphone() {
        return phone;
    }
    public String getType() {
        return type;
    }
    public String getAddress() {
        return address;
    }
    public String getColor() {
        return backgroundColor;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setColor(String color) {
        this.backgroundColor = color;
    }

}
