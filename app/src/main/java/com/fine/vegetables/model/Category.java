package com.fine.vegetables.model;

import java.util.List;

public class Category {
    private int id;
    private String name;
    private List<Commodity> commodities;

    public String getName(){
        return name;
    }
    public List<Commodity> getCommodities(){
        return commodities;
    }
}
