package com.example.domotica.repositories;

import android.provider.BaseColumns;

public class DeviceContract {
    private DeviceContract() {
    }

    public static class DeviceEntry implements BaseColumns {
        public static final String TABLE_NAME = "devices";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_IP = "ip";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DeviceEntry.TABLE_NAME + " (" +
                    DeviceEntry._ID + " INTEGER PRIMARY KEY," +
                    DeviceEntry.COLUMN_NAME_NAME + " TEXT," +
                    DeviceEntry.COLUMN_NAME_IP + " TEXT)";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DeviceEntry.TABLE_NAME;
}