package com.curryout.model;

import java.io.Serializable;

public class RestaurantDataModel implements Serializable {

    private String restrauntID;
    private String RestauName;
    private String foodName;
    private String address;
    private String cuisine_name;
    private int imgRestaurant;

    public String getRestrauntID() {
        return restrauntID;
    }

    public void setRestrauntID(String restrauntID) {
        this.restrauntID = restrauntID;
    }

    public RestaurantDataModel( String restauName, String foodName, String address,int imgRestaurant) {

        RestauName = restauName;
        this.foodName = foodName;
        this.address = address;
        this.imgRestaurant = imgRestaurant;

    }

    public RestaurantDataModel(String restrauntID,String restauName, String foodName, String address, int imgRestaurant) {
        this.restrauntID = restrauntID;
        RestauName = restauName;
        this.foodName = foodName;
        this.address = address;
        this.imgRestaurant = imgRestaurant;
    }

    public RestaurantDataModel(String restauName, String foodName, String address,String cuisine_name) {
        RestauName = restauName;
        this.foodName = foodName;
        this.address = address;
        this.cuisine_name = cuisine_name;
    }
    public String getRestauName() {
        return RestauName;
    }

    public void setRestauName(String restauName) {
        RestauName = restauName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getImgRestaurant() {
        return imgRestaurant;
    }

    public void setImgRestaurant(int imgRestaurant) {
        this.imgRestaurant = imgRestaurant;
    }


    @Override
    public String toString() {
        return RestauName;
    }
}
