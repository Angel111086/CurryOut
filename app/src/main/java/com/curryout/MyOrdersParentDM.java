package com.curryout;

public class MyOrdersParentDM {

    private String restName;
    private String foodName;
    private int restImg;

    public MyOrdersParentDM(String restName, String foodName, int restImg) {
        this.restName = restName;
        this.foodName = foodName;
        this.restImg = restImg;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getRestImg() {
        return restImg;
    }

    public void setRestImg(int restImg) {
        this.restImg = restImg;
    }
}
