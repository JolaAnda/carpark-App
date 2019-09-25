//
//  CarPark+CoreDataProperties.swift
//  Bonobo_iOS
//
//  Created by Ismael Abdelatif on 6/25/19.
//  Copyright © 2019 Administrator. All rights reserved.
//
//

import Foundation
import CoreData


extension CarPark {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<CarPark> {
        return NSFetchRequest<CarPark>(entityName: "CarPark")
    }

    @NSManaged public var address: String?
    @NSManaged public var disabledParking: Bool
    @NSManaged public var entranceHeight: Double
    @NSManaged public var familyParking: Bool
    @NSManaged public var id: UUID
    @NSManaged public var indoorParking: Bool
    @NSManaged public var latLng: String?
    @NSManaged public var name: String?
    @NSManaged public var openHours: String?
    @NSManaged public var security: Bool
    @NSManaged public var tarriff: String?
    @NSManaged public var totalPlaces: Int32
    @NSManaged public var womenParking: Bool

}
