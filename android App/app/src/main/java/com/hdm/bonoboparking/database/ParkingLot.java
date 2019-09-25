package com.hdm.bonoboparking.database;

import com.google.android.gms.maps.model.LatLng;

public class ParkingLot {

    //variables for the parkingLot Items
    int id;
    String name;
    String adress;
    String openHours;
    int totalPlaces;
    String tariff;
    String security;
    String entranceHeight;
    String womenParking;
    String disabledParking;
    String familyParking;
    String indoorParking;
    LatLng latLng;





    //build constructor
    public ParkingLot(int id, String name, String adress, String openHours, int totalPlaces, String tariff, String security, String entranceHeight, String womenParking, String disabledParking, String familyParking, String indoorParking){
        this.id = id;
        this.name= name;
        this.adress=adress;
        this.openHours = openHours;
        this.totalPlaces = totalPlaces;
        this.tariff = tariff;
        this.security = security;
        this.entranceHeight = entranceHeight;
        this.womenParking = womenParking;
        this.disabledParking = disabledParking;
        this.familyParking = familyParking;
        this.indoorParking = indoorParking;
    }


    //get and set methods

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public int getTotalPlaces() {
        return totalPlaces;
    }

    public void setTotalPlaces(int totalPlaces) {
        this.totalPlaces = totalPlaces;
    }

    public String getTariff() {
        return tariff;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    public String isSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getEntranceHeight() {
        return entranceHeight;
    }

    public void setEntranceHeight(String entranceHeight) {
        this.entranceHeight = entranceHeight;
    }

    public String isWomenParking() {
        return womenParking;
    }

    public void setWomenParking(String womenParking) {
        this.womenParking = womenParking;
    }

    public String isDisabledParking() {
        return disabledParking;
    }

    public void setDisabledParking(String disabledParking) {
        this.disabledParking = disabledParking;
    }

    public String isFamilyParking() {
        return familyParking;
    }

    public void setFamilyParking(String familyParking) {
        this.familyParking = familyParking;
    }

    public String isIndoorParking() {
        return indoorParking;
    }

    public void setIndoorParking(String indoorParking) {
        this.indoorParking = indoorParking;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }


    //toString method

    @Override
    public String toString() {
        return "Number: " + id +
                ", Name: " + name + '\n' +
                "Address: " + adress + '\n' +
                "Open Hours: " + openHours + '\n' +
                "Total Places: " + totalPlaces +
                ", Tariff: " + tariff + '\n' +
                "Security: " + security +
                ", Entrance Height: " + entranceHeight + '\n' +
                "WomenParking: " + womenParking + '\n' +
                "DisabledParking: " + disabledParking + '\n' +
                "Family Parking: " + familyParking +
                ", Indoor Parking: " + indoorParking;
    }
}
