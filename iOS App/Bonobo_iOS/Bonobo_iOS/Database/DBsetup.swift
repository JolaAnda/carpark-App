//
//  DBsetup.swift
//  Bonobo_iOS
//
//  Created by Ismael Abdelatif on 6/24/19.
//  Copyright © 2019 Administrator. All rights reserved.
//

import Foundation
import CoreData
import UIKit

//reset, delete, fill DB
//setup database with carpark data
 public class DBsetup{
    
    //reset database: first delete all data in the table then fill the db with new data
    static func resetDB(completion:() -> ()){
        
        deleteDB {
            fillDB()
        }
        
        completion()
    }
    

    
    //delete db CarPark
    static func deleteDB(completion: () -> ()){
        
        // create the delete request for the specified entity
        let fetchRequest: NSFetchRequest<CarPark> =  CarPark.fetchRequest()
        
        let deleteRequest = NSBatchDeleteRequest(fetchRequest: fetchRequest as! NSFetchRequest<NSFetchRequestResult>)
        
        
        //let container = NSPersistentContainer(name: "CarPark")
    
        do {
            try DBHelper.getContext.execute(deleteRequest)
            try DBHelper.getContext.save()
        } catch let error as NSError {
            print(error)
        }
        
        print("Core Data Deleted")
        
        completion()
    }
    
    
    
    //fill db with car park data and its attributes from carpark class
    static func fillDB(){
        
        //Saves the thing down below. Next step: Creating array and save all data in db
        //Build bottom sheet
        //Get all locations and show on map
        //create top nav
        
        let uuid = UUID.init()
        print(uuid)
        
        _ = CarPark( id: uuid, name: "Zueblin Parkhaus", address: "Lazarettstr. 5, 70182 Stuttgart", openHours: "24h", totalPlaces: 597, tarriff: "2.50 per hour", entranceHeight: 2.10, security: true, womenParking: true, disabledParking: true, familyParking: false, indoorParking: true, latLng: "123456789", context: DBHelper.getContext)
        
        _ = CarPark( id: uuid, name: "Breunninger Parkhaus", address: "Esslingerstraße 1, 70182 Stuttgart", openHours: "07.00-24.00", totalPlaces: 650, tarriff: "1h free, 1.20€ per 30 min", entranceHeight: 1.90, security: false, womenParking: true, disabledParking: true, familyParking: false, indoorParking: true, latLng: "123456789", context: DBHelper.getContext)
        
        _ = CarPark( id: uuid, name: "Tiefgarage Schwabenzentrum", address: "Hauptstätter Straße 40, 70173 Stuttgart", openHours: "24h", totalPlaces: 396, tarriff: "2.90€ per hour", entranceHeight: 2.0, security: true, womenParking: true, disabledParking: true, familyParking: false, indoorParking: true, latLng: "123456789", context: DBHelper.getContext)
        
        _ = CarPark( id: uuid, name: "Dorotheen-Quartier Parkgarage", address: "Holzstraße 21, 70182 Stuttgart", openHours: "Mo-Thu: 05.30-00.30, Fr-Sat: 05.30-01.30, Sun: 09.30-00.30", totalPlaces: 250, tarriff: "1.50€ per 30 min", entranceHeight: 0.0, security: false, womenParking: true, disabledParking: true, familyParking: false, indoorParking: true, latLng: "123456789", context: DBHelper.getContext)
        
        _ = CarPark( id: uuid, name: "Parkhaus Bohnenviertel", address: "Rosenstraße 27A, 70182 Stuttgart", openHours: "Mo-Sat: 08.00-23.00, Sun: 08.00-19.00", totalPlaces: 360, tarriff: "2.00€ per hour", entranceHeight: 2.0, security: false, womenParking: true, disabledParking: true, familyParking: false, indoorParking: true, latLng: "123456789", context: DBHelper.getContext)
        
        DBHelper.saveContext()
        
        print("CoreData filled up")
        
    }
    
}
