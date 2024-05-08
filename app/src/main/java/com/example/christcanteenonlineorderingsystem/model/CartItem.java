package com.example.christcanteenonlineorderingsystem.model;

import java.util.ArrayList;

public class CartItem {

    private String foodName;
    private ArrayList<Long> foodProps;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public ArrayList<Long> getFoodProps() {
        return foodProps;
    }

    public void setFoodProps(ArrayList<Long> foodProps) {
        this.foodProps = foodProps;
    }
}
