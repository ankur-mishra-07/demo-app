package com.demo.demoapplication.ui.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchListModel {


    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("image")
    @Expose
    private Integer image;


    public SearchListModel(String name, String email, Integer image) {
        this.name = name;
        this.email = email;
        this.image = image;
    }

    public SearchListModel(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public SearchListModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }
}