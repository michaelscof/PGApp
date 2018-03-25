package com.example.prem.pgapp;

/**
 * Created by Prem on 18-Mar-18.
 */

public class CustomerInformation {
    String email;
    String name;
    String password;
    String mobile;
    public CustomerInformation() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
