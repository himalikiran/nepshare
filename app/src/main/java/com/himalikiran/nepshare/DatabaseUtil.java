package com.himalikiran.nepshare;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by hbhusal on 2/13/2017.
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
