package com.example.sos_app_ui.logs.model;

import java.sql.Timestamp;

/**
 * Model of log to b saved in the external list
 */
public class LogModel {

    private Timestamp timestamp;
    private String message;

    public LogModel(Timestamp timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return timestamp +
                ", \n" + message;
    }
}
