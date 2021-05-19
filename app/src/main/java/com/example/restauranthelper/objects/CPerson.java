package com.example.restauranthelper.objects;

public class CPerson {
    private int ID;
    private String name;
    private int income;

    public CPerson(int ID, String name, int income) {
        this.ID = ID;
        this.name = name;
        this.income = income;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }
}
