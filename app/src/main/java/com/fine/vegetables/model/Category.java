package com.fine.vegetables.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category  implements Parcelable {
    private int groupId;
    private String name;
    private List<Commodity> commodities;

    public String getName(){
        return name;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCommodities(List<Commodity> commodities) {
        this.commodities = commodities;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public List<Commodity> getCommodities(){
        return commodities;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.groupId);
        dest.writeString(this.name);
        dest.writeList(this.commodities);
    }

    public Category() {
    }

    protected Category(Parcel in) {
        this.groupId = in.readInt();
        this.name = in.readString();
        this.commodities = new ArrayList<Commodity>();
        in.readList(this.commodities, List.class.getClassLoader());
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
