package com.example.christcanteenonlineorderingsystem.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartParcable implements Parcelable {
    private Map<String, ArrayList<Double>> cart;

    public CartParcable(Map<String, ArrayList<Double>> cart) {
        this.cart = cart;
    }

    protected CartParcable(Parcel in) {
        cart = new HashMap<>();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            ArrayList<Double> value = new ArrayList<>();
            in.readList(value, Long.class.getClassLoader());
            cart.put(key, value);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cart.size());
        for (Map.Entry<String, ArrayList<Double>> entry : cart.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeList(entry.getValue());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartParcable> CREATOR = new Creator<CartParcable>() {
        @Override
        public CartParcable createFromParcel(Parcel in) {
            return new CartParcable(in);
        }

        @Override
        public CartParcable[] newArray(int size) {
            return new CartParcable[size];
        }
    };

    public Map<String, ArrayList<Double>> getCart() {
        return cart;
    }
}
