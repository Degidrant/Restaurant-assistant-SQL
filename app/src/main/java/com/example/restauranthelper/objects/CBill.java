package com.example.restauranthelper.objects;

import java.sql.Date;
import java.sql.Time;

public class CBill {
    private int ID;
    private int sum;
    private int personID;
    private int tableID;
    private Date date;
    private Time time;

    public CBill(int ID, int sum, int personID, int tableID, Date date, Time time) {
        this.ID = ID;
        this.sum = sum;
        this.personID = personID;
        this.tableID = tableID;
        this.date = date;
        this.time = time;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
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
