package com.optionsmoneymaker.optionsmoneymaker.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Sagar on 07-10-2016.
 */
public class About {

    @Expose
    private Integer status = 0;
    @Expose
    private String data = "";

    /**
     * @return The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return The data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(String data) {
        this.data = data;
    }
}
