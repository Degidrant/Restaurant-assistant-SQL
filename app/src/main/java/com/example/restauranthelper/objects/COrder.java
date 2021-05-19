package com.example.restauranthelper.objects;

public class COrder {
    private int dishId;
    private int optionId;
    private int count;

    public COrder(int dishId, int optionId, int count) {
        this.dishId = dishId;
        this.optionId = optionId;
        this.count = count;
    }

    public COrder(int dishId) {
        this.dishId = dishId;
        this.optionId = -1;
        this.count = 1;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void add(){
        this.count++;
    }

}
