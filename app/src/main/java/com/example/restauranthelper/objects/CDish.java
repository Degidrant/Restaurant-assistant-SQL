package com.example.restauranthelper.objects;


public class CDish {
    private String name, type;
    private int price;
    private int ID;
    private int weight;

    public CDish(String name, String type, int price, int ID, int weight) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.ID = ID;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
