package com.optionsmoneymaker.optionsmoneymaker.model;

/**
 * Created by Akshata on 12/9/2017.
 */

public class MessageEvent {
    private String event;

    public MessageEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
