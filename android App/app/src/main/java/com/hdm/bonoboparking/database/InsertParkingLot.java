package com.hdm.bonoboparking.database;

import android.content.Context;

public class InsertParkingLot {


    private DataSource dataSource;

    //insert values into table to get data for the map
    public void insertInTable(Context context){

        dataSource = new DataSource(context);

        //create parkingLots with the class ParkingLot to use them in the insertParkingLot method
        ParkingLot züblin = new ParkingLot(1,"Züblin Parkhaus", "Lazarettstraße 5, 70182 Stuttgart", "24h", 597, "2.50€ per hour", "yes", "2.10m", "yes", "yes", null, "yes");
        ParkingLot breunninger = new ParkingLot(2, "Breunninger Parkhaus", "Esslingerstraße 1, 70182 Stuttgart", "07.00-24.00", 650, "1h free, 1.20€ per 30 min", null, "1.90m", "yes", "yes", "yes", "yes");
        ParkingLot schwabenzentrum = new ParkingLot(3, "Tiefgarage Schwabenzentrum", "Hauptstätter Straße 40, 70173 Stuttgart", "24h", 396, "2.90€ per hour", "yes", "2.00m", "yes", "yes", "yes", "yes");
        ParkingLot dorotheen = new ParkingLot(4, "Dorotheen-Quartier Parkgarage", "Holzstraße 21, 70182 Stuttgart", "Mo-Thu: 05.30-00.30, Fr-Sat: 05.30-01.30, Sun: 09.30-00.30", 250, "1.50€ per 30 min", null, null, "yes", "yes", "yes", "yes");
        ParkingLot bohnenviertel = new ParkingLot(5, "Parkhaus Bohnenviertel", "Rosenstraße 27A, 70182 Stuttgart", "Mo-Sat: 08.00-23.00, Sun: 08.00-19.00", 360, "2.00€ per hour", "no", "2.00m", "yes", "yes", null, "yes");

        //insert the values into the table
        dataSource.insertParkingLot(züblin);
        dataSource.insertParkingLot(breunninger);
        dataSource.insertParkingLot(schwabenzentrum);
        dataSource.insertParkingLot(dorotheen);
        dataSource.insertParkingLot(bohnenviertel);
    }

}
