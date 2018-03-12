package com.example.prem.pgapp;

/**
 * Created by Prem on 12-Mar-18.
 */

public class SaveCredentialsDB {
    public String email;
    public String password;
    public SaveCredentialsDB()
    {

    }

    public SaveCredentialsDB(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
