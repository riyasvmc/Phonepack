package com.vmc.white.model;

import java.io.Serializable;
import java.util.Locale;

public class User implements Serializable {

    private String name;
    private String category;
    private String phone;

    public User(){

    }

    public User(String name, String category, String phone) {
        this.name = name;
        this.category = category;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

