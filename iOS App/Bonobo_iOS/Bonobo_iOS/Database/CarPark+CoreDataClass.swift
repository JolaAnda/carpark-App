//
//  CarPark+CoreDataClass.swift
//  Bonobo_iOS
//
//  Created by Ismael Abdelatif on 6/24/19.
//  Copyright © 2019 Administrator. All rights reserved.
//
//

import Foundation
import CoreData


//attributes of carparks are listed here
//carpark object: set attributes
@objc(CarPark)
public class CarPark: NSManagedObject {
    
    convenience init(id: UUID, name: String, address: String, openHours: String, totalPlaces: Int, tarriff: String, entranceHeight: Double, security: Bool, womenParking: Bool, disabledParking: Bool, familyParking: Bool, indoorParking: Bool, latLng: String, context: NSManagedObjectContext) {
        
        if let ent = NSEntityDescription.entity(forEntityName: "CarPark", in: context){
            self.init(entity: ent, insertInto: context)
            
            self.id = id
            self.name = name
            self.address = address
            self.openHours = openHours
            self.totalPlaces = Int32(totalPlaces)
            self.tarriff = tarriff
            self.entranceHeight = entranceHeight
            self.security = security
            self.womenParking = womenParking
            self.disabledParking = disabledParking
            self.familyParking = familyParking
            self.indoorParking = indoorParking
            self.latLng = latLng
            
        } else {
            fatalError("Unable to find Entity Name")
        }
        
    }

}
