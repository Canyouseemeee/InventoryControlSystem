package com.bank.inventorycontrolsystem.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductOUT implements Parcelable {

    private String name;
    private String image;
    private String typename;
    private String productOutNo;
    private String productId;
    private String dateOut;
    private int quantity;
    private double price;

    public ProductOUT() {

    }

    public ProductOUT(String name, String image, String typename, String productOutNo, String productId, String dateIn, int quantity, double price) {
        this.name = name;
        this.image = image;
        this.typename = typename;
        this.productOutNo = productOutNo;
        this.productId = productId;
        this.dateOut = dateIn;
        this.quantity = quantity;
        this.price = price;
    }

    protected ProductOUT(Parcel in) {
        name = in.readString();
        image = in.readString();
        typename = in.readString();
        productOutNo = in.readString();
        productId = in.readString();
        dateOut = in.readString();
        quantity = in.readInt();
        price = in.readDouble();
    }

    public static final Creator<ProductOUT> CREATOR = new Creator<ProductOUT>() {
        @Override
        public ProductOUT createFromParcel(Parcel in) {
            return new ProductOUT(in);
        }

        @Override
        public ProductOUT[] newArray(int size) {
            return new ProductOUT[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getProductOutNo() {
        return productOutNo;
    }

    public void setProductOutNo(String productOutNo) {
        this.productOutNo = productOutNo;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDateIn() {
        return dateOut;
    }

    public void setDateIn(String dateIn) {
        this.dateOut = dateIn;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(typename);
        dest.writeString(productOutNo);
        dest.writeString(productId);
        dest.writeString(dateOut);
        dest.writeInt(quantity);
        dest.writeDouble(price);
    }
}
