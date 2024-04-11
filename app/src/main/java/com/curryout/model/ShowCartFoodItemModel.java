package com.curryout.model;

import java.io.Serializable;

public class ShowCartFoodItemModel implements Serializable {

    String name,quantity,price;
    String cart_id,pid;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
    public ShowCartFoodItemModel(){}
    public ShowCartFoodItemModel(String pid, String cart_id, String name, String quantity, String price) {
        this.pid = pid;
        this.cart_id = cart_id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
    public ShowCartFoodItemModel(String name, String quantity, String price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }
}
