package com.example.prem.pgapp;

/**
 * Created by Prem on 12-Mar-18.
 */

public class SaveCredentialsDB {
    public String email;
    public String password;
    public String name;
    public String mobile;
    public SaveCredentialsDB()
    {
    }

    public SaveCredentialsDB(String email, String password, String name, String mobile) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.mobile = mobile;
    }
}
