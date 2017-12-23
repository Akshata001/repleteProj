package com.optionsmoneymaker.optionsmoneymaker.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Sagar on 07-10-2016.
 */
public class ContactUS {

    @Expose
    private Integer status = 0;
    @Expose
    private ContactUSData data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ContactUSData getData() {
        return data;
    }

    public void setData(ContactUSData data) {
        this.data = data;
    }
}
