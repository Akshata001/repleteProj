package com.optionsmoneymaker.optionsmoneymaker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sagar on 13-10-2016.
 */
public class MessageDetail {

    @Expose
    private Integer status = 0;
    @SerializedName("title")
    @Expose
    private String title = "";
    @SerializedName("id")
    @Expose
    private String id = "";
    @Expose
    private String message = "";
    @SerializedName("product_name")
    @Expose
    private String productName = "";
    @SerializedName("date_time")
    @Expose
    private String dateTime = "";

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
