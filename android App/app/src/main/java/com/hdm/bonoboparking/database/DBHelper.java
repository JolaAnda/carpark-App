package com.hdm.bonoboparking.database;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.Context;

import androidx.annotation.VisibleForTesting;


//Helperclass for the database
//properties and Table creation need to be made with sql statements



public class DBHelper extends SQLiteOpenHelper {

    //Constant for logging
    private static final String LOG_TAG = DBHelper.class.getSimpleName();

    //set table Strings
    public static final String DB_NAME = "EasyCarPark.db";
    public static final int DB_VERSION = 2;
    public static final String TABLE_Car_Park = "carpark";

    //set columns
    public static final String COLUMN_ID = "id_carpark";
    public static final String COLUMN_name = "name_carpark";
    public static final String COLUMN_address = "address_id";
    public static final String COLUMN_hours = "openHours";
    public static final String COLUMN_totalPlaces = "places_total";
    public static final String COLUMN_tariff = "tariff_id";
    public static final String COLUMN_security = "security";
    public static final String COLUMN_height = "entranceHeight";
    public static final String COLUMN_women = "women_parking";
    public static final String COLUMN_disabled = "disabled_parking";
    public static final String COLUMN_family = "family_parking";
    public static final String COLUMN_indoor = "indoor_parking";




    // column_id is set with PRIMARY KEY AUTOINCREMENT, for automatic counting on id-column
    // create table statement with all the column names and data types
    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_Car_Park +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_name + " STRING NOT NULL, " +
                    COLUMN_address + " STRING NOT NULL, " +
                    COLUMN_hours + " STRING NOT NULL, " +
                    COLUMN_totalPlaces + " INTEGER NOT NULL, " +
                    COLUMN_tariff + " STRING NOT NULL, " +
                    COLUMN_security + " STRING, " +
                    COLUMN_height + " String, " +
                    COLUMN_women + " STRING, " +
                    COLUMN_disabled + " STRING," +
                    COLUMN_family + " STRING, " +
                    COLUMN_indoor + " STRING );";

    //statement for the deletion of te Table
    public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_Car_Park;




    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper creates Database: " + getDatabaseName());
    }


    //generation of the table for the database to insert values into the table
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "The table is generated with the sql-statement: " + SQL_CREATE + ".");
            db.execSQL(SQL_CREATE);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Error when generating the table: " + ex.getMessage());
        }

    }


    //delete the table when there is an upgrade and create the table new
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        onCreate(db);
    }

}
