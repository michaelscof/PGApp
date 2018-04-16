package com.example.prem.pgapp;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by root on 16/3/18.
 */

public class DatabaseUtil {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }

        return mDatabase;

    }
}
