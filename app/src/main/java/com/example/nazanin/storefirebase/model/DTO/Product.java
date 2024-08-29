package com.example.nazanin.storefirebase.model.DTO;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String id;
    private String productName;
    private String description;
    private int stock;
    private int price;
    private String image;
    private String category_id;
    private String date;
    private int salesCount;

    public Product(){

    }

    public Product(String id, String productName, String description, int stock, int price, String image, String category_id, String date, int salesCount) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.image = image;
        this.category_id = category_id;
        this.date = date;
        this.salesCount = salesCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(int salesCount) {
        this.salesCount = salesCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.productName);
        dest.writeString(this.description);
        dest.writeInt(this.stock);
        dest.writeInt(this.price);
        dest.writeString(this.image);
        dest.writeString(this.category_id);
        dest.writeInt(this.salesCount);
    }

    protected Product(Parcel in) {
        this.id = in.readString();
        this.productName = in.readString();
        this.description = in.readString();
        this.stock = in.readInt();
        this.price = in.readInt();
        this.image = in.readString();
        this.category_id = in.readString();
        this.salesCount = in.readInt();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
