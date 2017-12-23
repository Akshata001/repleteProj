package com.optionsmoneymaker.optionsmoneymaker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Sagar on 10/13/2016.
 */
public class MessageResult {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private ArrayList<MessageData> data = new ArrayList<MessageData>();

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
    public ArrayList<MessageData> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(ArrayList<MessageData> data) {
        this.data = data;
    }

}
