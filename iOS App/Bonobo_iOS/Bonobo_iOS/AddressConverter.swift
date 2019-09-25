//
//  AddressConverter.swift
//  Bonobo_iOS
//
//  Created by Ismael Abdelatif on 6/26/19.
//  Copyright © 2019 Administrator. All rights reserved.
//

import Foundation
import CoreLocation

class AddressConverter{
    
    static func convertStringToLatLng(address: String, name: String, completion: @escaping (CLLocationCoordinate2D, String, String) -> ()){
        
        let geocoder = CLGeocoder()
        
        
        geocoder.geocodeAddressString(address) { (placemarks, error) in
            guard
                let placemarks = placemarks,
                let location = placemarks.first?.location
                else {
                    print("No Location found")
                    return
            }
            
            completion(location.coordinate, address, name)
            
        }
    }
}
