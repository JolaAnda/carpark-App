package com.hdm.bonoboparking.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//this class is for the connection to the database: Data Access Object
//creation process of table is started

public class DataSource {

    //Constant for logging
    private static final String LOG_TAG = DataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    //column array with names of columns from table as projection for the cursor
    private String[] columns = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_name,
            DBHelper.COLUMN_address,
            DBHelper.COLUMN_hours,
            DBHelper.COLUMN_totalPlaces,
            DBHelper.COLUMN_tariff,
            DBHelper.COLUMN_security,
            DBHelper.COLUMN_height,
            DBHelper.COLUMN_women,
            DBHelper.COLUMN_disabled,
            DBHelper.COLUMN_family,
            DBHelper.COLUMN_indoor,

    };




    public DataSource(Context context) {
        Log.d(LOG_TAG, "Datasource generates dbHelper.");
        dbHelper = new DBHelper(context);
    }



    //set connection to database, with this method the generation of the database is started
    //the onCreate() Method from DBHelper is executed and the table is build
    public void open(){
        Log.d(LOG_TAG, "A reference to the database is requested.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Database-Reference received. Directory to database: " + database.getPath());
    }



    //close connection to database
    public void close(){
        dbHelper.close();
        Log.d(LOG_TAG, "Database is closed with DBHelper.");
    }





    //insert values for the table
    //therefore the database needs to be opened
    public void insertParkingLot(ParkingLot parkingLot){
        open();

        ContentValues values = new ContentValues();

        values.put(DBHelper.COLUMN_name, parkingLot.getName());
        values.put(DBHelper.COLUMN_address, parkingLot.getAdress());
        values.put(DBHelper.COLUMN_hours, parkingLot.getOpenHours());
        values.put(DBHelper.COLUMN_totalPlaces, parkingLot.getTotalPlaces());
        values.put(DBHelper.COLUMN_tariff, parkingLot.getTariff());
        values.put(DBHelper.COLUMN_security, parkingLot.isSecurity());
        values.put(DBHelper.COLUMN_height, parkingLot.getEntranceHeight());
        values.put(DBHelper.COLUMN_women, parkingLot.isWomenParking());
        values.put(DBHelper.COLUMN_disabled, parkingLot.isDisabledParking());
        values.put(DBHelper.COLUMN_family, parkingLot.isFamilyParking());
        values.put(DBHelper.COLUMN_indoor, parkingLot.isIndoorParking());


        long insertID = database.insert(DBHelper.TABLE_Car_Park, null, values);


    }




    //update data in table if data needs to be changed / updated
    public ParkingLot updateShoppingMemo(ParkingLot parkingLot) {


        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_name, parkingLot.getName());
        values.put(DBHelper.COLUMN_address, parkingLot.getAdress());
        values.put(DBHelper.COLUMN_hours, parkingLot.getOpenHours());
        values.put(DBHelper.COLUMN_totalPlaces, parkingLot.getTotalPlaces());
        values.put(DBHelper.COLUMN_tariff, parkingLot.getTariff());
        values.put(DBHelper.COLUMN_security, parkingLot.isSecurity());
        values.put(DBHelper.COLUMN_height, parkingLot.getEntranceHeight());
        values.put(DBHelper.COLUMN_women, parkingLot.isWomenParking());
        values.put(DBHelper.COLUMN_disabled, parkingLot.isDisabledParking());
        values.put(DBHelper.COLUMN_family, parkingLot.isFamilyParking());
        values.put(DBHelper.COLUMN_indoor, parkingLot.isIndoorParking());


        database.update(DBHelper.TABLE_Car_Park,
                values,
                DBHelper.COLUMN_ID + "=" + parkingLot.getId(),
                null);

        Cursor cursor = database.query(DBHelper.TABLE_Car_Park,
                columns, DBHelper.COLUMN_ID + "=" + parkingLot.getId(),
                null, null, null, null);

        cursor.moveToFirst();
        ParkingLot parkingLotChanged = cursorToGetData(cursor);
        cursor.close();

        return parkingLotChanged;
    }



    //Helper Class for Cursor
    //select Indices of table columns
    //get-Method
    private ParkingLot cursorToGetData(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(DBHelper.COLUMN_name);
        int idAddress = cursor.getColumnIndex(DBHelper.COLUMN_address);
        int idHours = cursor.getColumnIndex(DBHelper.COLUMN_hours);
        int idTotal = cursor.getColumnIndex(DBHelper.COLUMN_totalPlaces);
        int idTariff = cursor.getColumnIndex(DBHelper.COLUMN_tariff);
        int idSecurity = cursor.getColumnIndex(DBHelper.COLUMN_security);
        int idHeight = cursor.getColumnIndex(DBHelper.COLUMN_height);
        int idWomen = cursor.getColumnIndex(DBHelper.COLUMN_women);
        int idDisabled = cursor.getColumnIndex(DBHelper.COLUMN_disabled);
        int idFamily = cursor.getColumnIndex(DBHelper.COLUMN_family);
        int idIndoor = cursor.getColumnIndex(DBHelper.COLUMN_indoor);


        int id = cursor.getInt(idIndex);
        String Name = cursor.getString(idName);
        String Address = cursor.getString(idAddress);
        String Hours = cursor.getString(idHours);
        int Total = cursor.getInt(idTotal);
        String Tariff = cursor.getString(idTariff);
        String Security = cursor.getString(idSecurity);
        String Height = cursor.getString(idHeight);
        String Women = cursor.getString(idWomen);
        String Disabled = cursor.getString(idDisabled);
        String Family = cursor.getString(idFamily);
        String Indoor = cursor.getString(idIndoor);



        ParkingLot parkingLot = new ParkingLot(id, Name, Address, Hours, Total, Tariff, Security, Height, Women, Disabled, Family, Indoor);

        return parkingLot;
    }


    //get all parking spaces from the table in a HashMap to set them in the google Map as markers
    public HashMap <String, ParkingLot> getAllParkingSpacesAsMap(){

        Cursor cursor = database.query(DBHelper.TABLE_Car_Park, columns, null, null, null, null, null);
        cursor.moveToFirst();

        HashMap <String, ParkingLot> carParkHashMap = new HashMap<>();

        ParkingLot parkingLot;

        //read out all data and convert them into ParkingLot-Objects and add them to the list
        while(!cursor.isAfterLast()){

            parkingLot = cursorToGetData(cursor);
            carParkHashMap.put(parkingLot.getName(), parkingLot);

            cursor.moveToNext();
        }

        cursor.close();

        //return of the complete list with all parking spaces as String
        return carParkHashMap;

    }



}
