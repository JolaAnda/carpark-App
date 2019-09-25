//
//  DBHelper.swift
//  Bonobo_iOS
//
//  Created by Ismael Abdelatif on 6/24/19.
//  Copyright © 2019 Administrator. All rights reserved.
//

import Foundation
import CoreData


//get data from db to show carparks on map
class DBHelper{
    
    
    private init(){}
    
    
    //get all car parks from db
    static func getAllCarParks(completion: ([CarPark]) -> Void) {
        
        var carParks = [CarPark] ()
        let fetchRequest: NSFetchRequest<CarPark> = CarPark.fetchRequest()
        
        do{
            carParks = try DBHelper.getContext.fetch(fetchRequest)
            
        } catch {}
        
        print(carParks)
        
        completion(carParks)
        
    }
    
    //get carpark by name from db
    static func getCarParkByName(name: String, completion: ([CarPark]) -> Void) {
        
        
        let result: [CarPark]
        let context = DBHelper.getContext
        
        let fetchRequest: NSFetchRequest<CarPark> = CarPark.fetchRequest()
        
        fetchRequest.predicate = NSPredicate(format: "name == %@", name)
        
        
        do {
            
            result = try context.fetch(fetchRequest)
            
            
            if result != nil {
                completion(result)
            }
            
            
            
            
        } catch{}
        
       
        
    }
    
    static var getContext: NSManagedObjectContext {
        print(persistentContainer.persistentStoreCoordinator.persistentStores.first?.url)  
        return persistentContainer.viewContext
    }
    
    static var getPersistentContainer: NSPersistentContainer {
        return persistentContainer
    }
    // MARK: - Core Data stack
    
    static var persistentContainer: NSPersistentContainer = {
        /*
         The persistent container for the application. This implementation
         creates and returns a container, having loaded the store for the
         application to it. This property is optional since there are legitimate
         error conditions that could cause the creation of the store to fail.
         */
        let container = NSPersistentContainer(name: "CarParks")
        container.loadPersistentStores(completionHandler: { (storeDescription, error) in
            if let error = error as NSError? {
                // Replace this implementation with code to handle the error appropriately.
                // fatalError() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
                
                /*
                 Typical reasons for an error here include:
                 * The parent directory does not exist, cannot be created, or disallows writing.
                 * The persistent store is not accessible, due to permissions or data protection when the device is locked.
                 * The device is out of space.
                 * The store could not be migrated to the current model version.
                 Check the error message to determine what the actual problem was.
                 */
                fatalError("Unresolved error \(error), \(error.userInfo)")
            }
        })
        return container
    }()
    
    // MARK: - Core Data Saving support
    
    static func saveContext () {
        
        let context = persistentContainer.viewContext
        if context.hasChanges {
            do {
                try context.save()
            } catch {
                // Replace this implementation with code to handle the error appropriately.
                // fatalError() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
                let nserror = error as NSError
                fatalError("Unresolved error \(nserror), \(nserror.userInfo)")
            }
        }
    }

}
