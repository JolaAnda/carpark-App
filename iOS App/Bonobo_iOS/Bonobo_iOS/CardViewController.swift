//
//  CardViewController.swift
//  CardViewAnimation
//
//  Created by Brian Advent on 26.10.18.
//  Copyright © 2018 Brian Advent. All rights reserved.
//

import UIKit

class CardViewController: UIViewController {
    
    
    @IBOutlet weak var handleArea: UIView!
    
    @IBOutlet weak var carParkName: UILabel!
    
    @IBOutlet weak var carParkDistance: UILabel!
    
    @IBOutlet weak var carParkSpace: UILabel!
    
    @IBOutlet weak var addressField: UILabel!
    
    @IBOutlet weak var openTimesField: UILabel!
    
    @IBOutlet weak var tarriffField: UILabel!
    
    @IBOutlet weak var entranceHeight: UILabel!
    
    @IBOutlet weak var disabledIcon: UIImageView!
    
    @IBOutlet weak var securityIcon: UIImageView!
    
    @IBOutlet weak var roofIcon: UIImageView!
    
    @IBOutlet weak var womenIcon: UIImageView!
    
    @IBOutlet weak var familyIcon: UIImageView!
    
    func resetInfoCard(){
        
        carParkName.text = "Click on Car Park"
        carParkSpace.text = ""
        carParkDistance.text = ""
        addressField.text = ""
        openTimesField.text = ""
        tarriffField.text = ""
        entranceHeight.text = ""
        
        disabledIcon.isHidden = true
        securityIcon.isHidden = true
        roofIcon.isHidden = true
        womenIcon.isHidden = true
        familyIcon.isHidden = true
        
        
    }
    
    
    func fillInfoCard(name: String, distance: Double){
        DBHelper.getCarParkByName(name: name) {
            input in
            
            if input.first != nil {
                
                var carPark = input.first!
                
                let distanceInKm = round((distance/1000)*100)/100
                
                if carPark.name != nil {
                    carParkName.text = carPark.name
                    carParkSpace.text = String(carPark.totalPlaces) + " places"
                    entranceHeight.text = String(carPark.entranceHeight) + "m"
                    carParkDistance.text = String(distanceInKm) + "km away"
                    addressField.text = carPark.address
                    openTimesField.text = carPark.openHours
                    tarriffField.text = carPark.tarriff
                    
                    if carPark.disabledParking {
                        disabledIcon.isHidden = false
                    }
                    if carPark.security {
                        securityIcon.isHidden = false
                    }
                    
                    if carPark.indoorParking {
                        roofIcon.isHidden = false
                    }
                    
                    if carPark.womenParking {
                        roofIcon.isHidden = false
                    }
                    
                    if carPark.familyParking {
                        familyIcon.isHidden = false
                    }
                }
            }
        }
    }
}
