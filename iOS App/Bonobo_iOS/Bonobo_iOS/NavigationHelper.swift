//
//  NavigationHelper.swift
//  Bonobo_iOS
//
//  Created by Ismael Abdelatif on 6/27/19.
//  Copyright © 2019 Administrator. All rights reserved.
//

import Foundation
import CoreLocation
import MapKit

//functions for navigation help:
//1 create MapKitItem
//2 calculate destination
class NavigationHelper {
    
    //create MapKitItem with placemark on map to show carpark
    func createMapKitItem(coordinates: CLLocationCoordinate2D) -> MKMapItem{
        
        let myPlacemark = MKPlacemark(coordinate: coordinates)
        
        let myMapItem = MKMapItem(placemark: myPlacemark)
        
        return myMapItem
    }
    
    
    //calculate destination between source coordinates and destination coordinates
    //to show the destination of carparks from the position of the user on the map
    func getRouteToDestination(srcCoordinates: CLLocationCoordinate2D?, destCoordinates: CLLocationCoordinate2D?, completion: @escaping (MKRoute) ->()){
        
        let srcmapItem = createMapKitItem(coordinates: srcCoordinates!)
        let destmapItem = createMapKitItem(coordinates: destCoordinates!)
        
        let request = MKDirections.Request() //create a direction request object
        request.source = srcmapItem //this is the source location mapItem object
        request.destination = destmapItem //this is the destination location mapItem object
        request.transportType = MKDirectionsTransportType.automobile //define the transportation method
        
        let directions = MKDirections(request: request) //request directions
        directions.calculate { (response, error) in
            guard let directionResponse = response else {
                
                if let error = error {
                    print("Error when fetching directions==\(error.localizedDescription)")
                }
                return
            }
            
            completion(directionResponse.routes[0])
        }
    }
    
}
