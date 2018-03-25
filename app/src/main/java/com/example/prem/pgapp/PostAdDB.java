package com.example.prem.pgapp;

/**
 * Created by Prem on 22-Mar-18.
 */

public class PostAdDB {
    public String name,address,location,landmark,image,contact,ownerid;
    public int seater,price;
    public boolean boys,girls,ac,wifi,food,maid,laundry;
    public PostAdDB()
    {
        this.name = null;
        this.address = null;
        this.location = null;
        this.landmark = null;
        this.image = null;
        this.seater = 0;
        this.price = 0;
        this.contact = null;
        this.boys = false;
        this.girls = false;
        this.ac = false;
        this.wifi = false;
        this.food = false;
        this.maid = false;
        this.laundry = false;
        this.ownerid = null;
    }
    public PostAdDB(String name, String address, String location, String landmark, String image,int seater, int price, String contact, boolean boys, boolean girls, boolean ac, boolean wifi, boolean food, boolean maid, boolean laundry,String ownerid) {
        this.name = name;
        this.address = address;
        this.location = location;
        this.landmark = landmark;
        this.image = image;
        this.seater = seater;
        this.price = price;
        this.contact = contact;
        this.boys = boys;
        this.girls = girls;
        this.ac = ac;
        this.wifi = wifi;
        this.food = food;
        this.maid = maid;
        this.laundry = laundry;
        this.ownerid = ownerid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public int getSeater() {
        return seater;
    }

    public void setSeater(int seater) {
        this.seater = seater;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public boolean isBoys() {
        return boys;
    }

    public void setBoys(boolean boys) {
        this.boys = boys;
    }

    public boolean isGirls() {
        return girls;
    }

    public void setGirls(boolean girls) {
        this.girls = girls;
    }

    public boolean isAc() {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isFood() {
        return food;
    }

    public void setFood(boolean food) {
        this.food = food;
    }

    public boolean isMaid() {
        return maid;
    }

    public void setMaid(boolean maid) {
        this.maid = maid;
    }

    public boolean isLaundry() {
        return laundry;
    }

    public void setLaundry(boolean laundry) {
        this.laundry = laundry;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }
}
