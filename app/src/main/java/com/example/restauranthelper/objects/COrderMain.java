package com.example.restauranthelper.objects;

import java.sql.Date;
import java.sql.Time;

public class COrderMain {
    private int ID;
    private Time time;
    private Date date;
    private int status;
    private int person;
    private int table;
    private String comment;

    public COrderMain(int ID, Time time, Date date, int status, int person, int table, String comment) {
        this.ID = ID;
        this.time = time;
        this.date = date;
        this.status = status;
        this.person = person;
        this.table = table;
        this.comment = comment;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
