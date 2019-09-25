//
//  CustomPin.swift
//  Bonobo_iOS
//
//  Created by Ismael Abdelatif on 6/27/19.
//  Copyright © 2019 Administrator. All rights reserved.
//

import MapKit

class CustomPin: NSObject, MKAnnotation {
    var coordinate: CLLocationCoordinate2D
    var title: String?
    var subtitle: String?
    
    
    init(pinTitle: String, pinSubTitle: String, location: CLLocationCoordinate2D){
        self.title = pinTitle
        self.subtitle = pinSubTitle
        self.coordinate = location
    }
    
}
