package com.optionsmoneymaker.optionsmoneymaker.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Sagar on 08-10-2016.
 */
public class ContactUSData {

    @Expose
    private String email = "";
    @Expose
    private String website = "";

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
