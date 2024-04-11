package com.curryout;

public class SearchDataModel {

    private String foodName;
    private String restQnty;
    private int imgId;

    public SearchDataModel(String foodName, String restQnty, int imgId) {
        this.foodName = foodName;
        this.restQnty = restQnty;
        this.imgId = imgId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getRestQnty() {
        return restQnty;
    }

    public void setRestQnty(String restQnty) {
        this.restQnty = restQnty;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
