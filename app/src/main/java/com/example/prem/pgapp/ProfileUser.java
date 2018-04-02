package com.example.prem.pgapp;

/**
 * Created by Prem on 03-Apr-18.
 */

public class ProfileUser {
    public String name,address,contact,occupation,email,image;
    public boolean single,married,male,female;
    public int age;

    public ProfileUser(String name, String address, String contact, String occupation, String email, String image, boolean single, boolean married, boolean male, boolean female, int age) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.occupation = occupation;
        this.email = email;
        this.image = image;
        this.single = single;
        this.married = married;
        this.male = male;
        this.female = female;
        this.age = age;
    }
    public ProfileUser()
    {

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public boolean isMarried() {
        return married;
    }

    public void setMarried(boolean married) {
        this.married = married;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public boolean isFemale() {
        return female;
    }

    public void setFemale(boolean female) {
        this.female = female;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
