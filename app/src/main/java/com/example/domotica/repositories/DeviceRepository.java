package com.example.domotica.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.domotica.domain.Device;

import java.util.ArrayList;
import java.util.List;

public class DeviceRepository extends Repository {
    private DeviceDbHelper dbHelper;

    public DeviceRepository(Context context) {
        dbHelper = new DeviceDbHelper(context);
    }

    public void remove(Context context, Device device) {

    }

    public List<Device> list() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DeviceContract.DeviceEntry._ID,
                DeviceContract.DeviceEntry.COLUMN_NAME_NAME,
                DeviceContract.DeviceEntry.COLUMN_NAME_IP
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DeviceContract.DeviceEntry.COLUMN_NAME_NAME + " DESC";

        Cursor cursor = db.query(
                DeviceContract.DeviceEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List<Device> devices = new ArrayList<>();
        while (cursor.moveToNext()) {
            Device device=new Device();
            device.ID = cursor.getLong(cursor.getColumnIndexOrThrow(DeviceContract.DeviceEntry._ID));
            device.Name = cursor.getString(cursor.getColumnIndexOrThrow(DeviceContract.DeviceEntry.COLUMN_NAME_NAME));
            device.Ip = cursor.getString(cursor.getColumnIndexOrThrow(DeviceContract.DeviceEntry.COLUMN_NAME_IP));

            devices.add(device);
        }
        cursor.close();

        return devices;
    }

    public void save(Device device) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DeviceContract.DeviceEntry.COLUMN_NAME_NAME, device.Name.toString());
        values.put(DeviceContract.DeviceEntry.COLUMN_NAME_IP, device.Ip.toString());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DeviceContract.DeviceEntry.TABLE_NAME, null, values);
        device.ID = newRowId;
    }
}
