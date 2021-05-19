package com.example.restauranthelper.objects;

public class CTable {
    private int ID;
    private int status;

    public CTable(int ID, int status) {
        this.ID = ID;
        this.status = status;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
