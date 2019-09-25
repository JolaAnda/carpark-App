//
//  Bonobo_iOSTests.swift
//  Bonobo_iOSTests
//
//  Created by Ismael Abdelatif on 7/1/19.
//  Copyright © 2019 Administrator. All rights reserved.
//

import XCTest
import CoreLocation
@testable import Bonobo_iOS

class Bonobo_iOSTests: XCTestCase {

    override func setUp() {
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }

    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }

    func testAddressConverter() {
        
        let expectedCoordinates = CLLocationCoordinate2D(latitude: 48.7729977, longitude: 9.1814079)
        
        AddressConverter.convertStringToLatLng(address: "Lazarettstr. 5, 70182 Stuttgart", name: "Zueblin Parkhaus") { (coordinates, address, name) in
            
            XCTAssertEqual(coordinates.latitude, expectedCoordinates.latitude, "Not the same latitude")
            
            XCTAssertEqual(coordinates.longitude, expectedCoordinates.longitude, "Not the expected longitude")
            
            
        }
        
    }

    func testPerformanceExample() {
        // This is an example of a performance test case.
        self.measure {
            // Put the code you want to measure the time of here.
        }
    }

}
