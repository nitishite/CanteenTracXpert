package com.example.christcanteenonlineorderingsystem.model;

import java.util.List;

public class AdminItem {
    private String id;
    private String name;
    private List<String> blockName;
    private String variant;
    private long availableQuantity;
    private double price;

    // Constructor
    public AdminItem(String id, String name, List<String> blockName, String variant, long availableQuantity, double price) {
        this.id = id;
        this.name = name;
        this.blockName = blockName;
        this.variant = variant;
        this.availableQuantity = availableQuantity;
        this.price = price;
    }

    public AdminItem() {

    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getBlockName() {
        return blockName;
    }

    public String getVariant() {
        return variant;
    }

    public long getAvailableQuantity() {
        return availableQuantity;
    }

    public double getPrice() {
        return price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBlockName(List<String> blockName) {
        this.blockName = blockName;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public void setAvailableQuantity(long availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
