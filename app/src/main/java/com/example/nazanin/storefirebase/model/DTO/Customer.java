package com.example.nazanin.storefirebase.model.DTO;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer implements Parcelable {
    private String id;
    private String name;
    private String lastname;
    private String email;
    private String password;
    private int credit;
    private boolean isActive=true;

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.lastname);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeInt(this.credit);
        dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
    }

    public Customer() {
    }

    protected Customer(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.lastname = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.credit = in.readInt();
        this.isActive = in.readByte() != 0;
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel source) {
            return new Customer(source);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };
}
