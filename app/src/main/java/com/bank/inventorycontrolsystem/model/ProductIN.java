package com.bank.inventorycontrolsystem.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductIN implements Parcelable {

    private String name;
    private String image;
    private String typename;
    private String productIdNo;
    private String productId;
    private String dateIn;
    private int quantity;
    private double price;

    public ProductIN() {

    }

    public ProductIN(String name, String image, String typename, String productIdNo, String productId, String dateIn, int quantity, double price) {
        this.name = name;
        this.image = image;
        this.typename = typename;
        this.productIdNo = productIdNo;
        this.productId = productId;
        this.dateIn = dateIn;
        this.quantity = quantity;
        this.price = price;
    }

    protected ProductIN(Parcel in) {
        name = in.readString();
        image = in.readString();
        typename = in.readString();
        productIdNo = in.readString();
        productId = in.readString();
        dateIn = in.readString();
        quantity = in.readInt();
        price = in.readDouble();
    }

    public static final Creator<ProductIN> CREATOR = new Creator<ProductIN>() {
        @Override
        public ProductIN createFromParcel(Parcel in) {
            return new ProductIN(in);
        }

        @Override
        public ProductIN[] newArray(int size) {
            return new ProductIN[size];
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

    public String getProductIdNo() {
        return productIdNo;
    }

    public void setProductIdNo(String productIdNo) {
        this.productIdNo = productIdNo;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDateIn() {
        return dateIn;
    }

    public void setDateIn(String dateIn) {
        this.dateIn = dateIn;
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
        dest.writeString(productIdNo);
        dest.writeString(productId);
        dest.writeString(dateIn);
        dest.writeInt(quantity);
        dest.writeDouble(price);
    }
}
