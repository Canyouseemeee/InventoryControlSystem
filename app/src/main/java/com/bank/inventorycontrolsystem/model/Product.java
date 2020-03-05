package com.bank.inventorycontrolsystem.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String productId;
    private String productName;
    private double price;
    private int qty;
    private String image;
    private String typeId;
    private String Unit_id;
    private String typeName;
    private String Unit_name;


    public Product() {

    }

    public Product(String productId, String productName, double price, int qty, String image, String typeId,String Unit_id, String typeName,String Unit_name) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.qty = qty;
        this.image = image;
        this.typeId = typeId;
        this.Unit_id = Unit_id;
        this.typeName = typeName;
        this.Unit_name = Unit_name;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getUnit_id() {
        return Unit_id;
    }

    public void setUnit_id(String unit_id) {
        Unit_id = unit_id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getUnit_name() {
        return Unit_name;
    }

    public void setUnit_name(String unit_name) {
        Unit_name = unit_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productId);
        dest.writeString(this.productName);
        dest.writeDouble(this.price);
        dest.writeInt(this.qty);
        dest.writeString(this.image);
        dest.writeString(this.typeId);
        dest.writeString(this.Unit_id);
        dest.writeString(this.typeName);
        dest.writeString(this.Unit_name);
    }

    protected Product(Parcel in) {
        this.productId = in.readString();
        this.productName = in.readString();
        this.price = in.readDouble();
        this.qty = in.readInt();
        this.image = in.readString();
        this.typeId = in.readString();
        this.Unit_id = in.readString();
        this.typeName = in.readString();
        this.Unit_name = in.readString();
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
