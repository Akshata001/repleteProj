package com.optionsmoneymaker.optionsmoneymaker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sagar on 10/13/2016.
 */
public class MessageData {

    @SerializedName("title")
    @Expose
    private String title = "";
    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("product_name")
    @Expose
    private String productName = "";
    @SerializedName("date_time")
    @Expose
    private String dateTime = "";
    @SerializedName("message")
    @Expose
    private String message = "";
    @SerializedName("isRead")
    @Expose
    private String isRead = "";

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}
