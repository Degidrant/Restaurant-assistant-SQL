package com.example.restauranthelper.objects;

import java.sql.Date;
import java.sql.Time;

public class CMessage {
    private String name;
    private String message;
    private Date date;
    private Time time;

    public CMessage(String name, String message, Date date, Time time) {
        this.name = name;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
