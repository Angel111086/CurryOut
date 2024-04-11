package com.curryout;

public class SearchGridDataModel {

    private String title;
    private String price;
    private int imgTitle;
    private String productID;

    public SearchGridDataModel(String title, String price, int imgTitle) {
        this.title = title;
        this.price = price;
        this.imgTitle = imgTitle;
    }

    public SearchGridDataModel(String productID,String title, String price, int imgTitle) {
        this.productID = productID;
        this.title = title;
        this.price = price;
        this.imgTitle = imgTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImgTitle() {
        return imgTitle;
    }

    public void setImgTitle(int imgTitle) {
        this.imgTitle = imgTitle;
    }
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

}
