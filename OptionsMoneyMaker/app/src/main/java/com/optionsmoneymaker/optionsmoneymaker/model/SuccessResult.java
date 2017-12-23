package com.optionsmoneymaker.optionsmoneymaker.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Sagar on 04-10-2016.
 */
public class SuccessResult {

    @Expose
    private Integer status = 0;
    @Expose
    private String message = "";
    @Expose
    private String userId = "";
    @Expose
    private String email = "";
    @Expose
    private String firstName = "";
    @Expose
    private String lastName = "";
    @Expose
    private String levelName = "";
    @Expose
    private String data = "";

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}