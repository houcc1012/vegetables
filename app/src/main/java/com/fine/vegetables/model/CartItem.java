package com.fine.vegetables.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private Integer id;
    private String name;
    private String logo;
    private Double price;
    private String unitName;
    private int count;
    private boolean selected;

    public Integer getId() {
        return id;
    }

    public String getUnitName() {
        return unitName;
    }

    public String getLogo() {
        return logo;
    }

    public Double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.logo);
        dest.writeValue(this.price);
        dest.writeString(this.unitName);
        dest.writeInt(this.count);
        dest.writeByte(selected ? (byte) 1 : (byte) 0);
    }

    public CartItem() {
    }

    protected CartItem(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.logo = in.readString();
        this.price = (Double) in.readValue(Double.class.getClassLoader());
        this.unitName = in.readString();
        this.count = in.readInt();
        this.selected = in.readByte() != 0;
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        public CartItem createFromParcel(Parcel source) {
            return new CartItem(source);
        }

        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };
}
