package com.example.christcanteenonlineorderingsystem.model;

public class MenuItem {
    private int AvailableQuantity;
    private double Price;
    private String Name, Variant, StoreName, BlockName;

    public MenuItem(String Name, double price) {
        this.Name = Name;
        this.Price = price;
    }

    public int getAvailableQuantity() {
        return AvailableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        AvailableQuantity = availableQuantity;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(long price) {
        Price = price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getVariant() {
        return Variant;
    }

    public void setVariant(String variant) {
        Variant = variant;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getBlockName() {
        return BlockName;
    }

    public void setBlockName(String blockName) {
        BlockName = blockName;
    }
}
