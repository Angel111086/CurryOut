package com.curryout.model;

public class SearchDataModel {

            String cuisineID,name,parent_id,status,restraunt_count;
            int img;
    public SearchDataModel(String cuisineID,String name, String restraunt_count)
    {
        this.cuisineID = cuisineID;
        this.name = name;
        this.restraunt_count = restraunt_count;
    }
    public SearchDataModel(String cuisineID,String name, String restraunt_count,int img)
    {
        this.cuisineID = cuisineID;
        this.name = name;
        this.restraunt_count = restraunt_count;
        this.img = img;
    }

    public String getRestraunt_count() {
        return restraunt_count;
    }

    public void setRestraunt_count(String restraunt_count) {
        this.restraunt_count = restraunt_count;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public SearchDataModel(String name, int img) {
        this.name = name;
        this.img = img;
    }
    public String getCuisineID() {
        return cuisineID;
    }

    public void setCuisineID(String cuisineID) {
        this.cuisineID = cuisineID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
