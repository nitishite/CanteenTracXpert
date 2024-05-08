package com.example.christcanteenonlineorderingsystem.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    private double price;
    private String blockName, itemName, storeName, variant;

    public Item() {
        // Default constructor required by Firebase
    }

    public Item(double price, String blockName, String itemName, String storeName, String variant) {
        this.price = price;
        this.blockName = blockName;
        this.itemName = itemName;
        this.storeName = storeName;
        this.variant = variant;
    }

    protected Item(Parcel in) {
        price = in.readDouble();
        blockName = in.readString();
        itemName = in.readString();
        storeName = in.readString();
        variant = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(price);
        dest.writeString(blockName);
        dest.writeString(itemName);
        dest.writeString(storeName);
        dest.writeString(variant);
    }
}
