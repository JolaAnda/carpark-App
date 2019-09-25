//
//  ViewController.swift
//  Bonobo_iOS
//
//  Created by Administrator on 21.05.19.
//  Copyright © 2019 Administrator. All rights reserved.
//

import UIKit
import MapKit
import CoreLocation

class ViewController: UIViewController, MKMapViewDelegate, CLLocationManagerDelegate {
    
    //initialize all UI elements
    @IBOutlet weak var mapView: MKMapView!
    
    @IBOutlet weak var leftBtn: UIImageView!

    @IBOutlet weak var rightBtn: UIImageView!

    @IBOutlet weak var navigationLabel: UILabel!
    
    
    //initialize all necessary variables
    let locationManager = CLLocationManager()
    
    var runningAnimations = [UIViewPropertyAnimator]()
    var animationProgressWhenInterrupted:CGFloat = 0
    
    var cardViewController: CardViewController!
    var visualEffectView: UIVisualEffectView!
    
    var currentDestinationCoordinates: CLLocationCoordinate2D!
    var currentDestinationName: String!
    
    let cardHeight:CGFloat = 600
    let cardHandleAreaHeight:CGFloat = 85
    
    //state of cardview
    enum CardState{
        case expanded
        case collapsed
    }
    
    //change state of cardview to collapsed or expanded
    var nextState:CardState{
        return cardVisible ? .collapsed : .expanded
    }
    
    var route: MKRoute!
    
    //at beginning bottom sheet not visible
    var cardVisible = false
    
    var infosVisible = false
    
    // start loading view functions
    override func viewDidLoad() {
        super.viewDidLoad()
        
        

        
        //setup Db: delete first, insert carpark data
        //after reset of db other functions are executed
        DBsetup.resetDB(completion: {
            fillMap()
            setupButton()
            setupCard()
            cardViewController.resetInfoCard()
            checkLocationServices()
        })

        
        
        
        self.mapView.delegate = self
        
    }
    
    
    
    //first check if infosVisible are true or false: adapt UI elements
    //set TapRecognizer to buttons with action call
    func setupButton(){
        changeNavigation()
        let leftTapRecognizer = UITapGestureRecognizer(target: self, action: #selector(leftImageTapped(leftTapRecognizer:)))
        leftBtn.isUserInteractionEnabled = true
        leftBtn.addGestureRecognizer(leftTapRecognizer)
        
        let rightTapRecognizer = UITapGestureRecognizer(target: self, action: #selector(rightImageTapped(rightTapRecognizer:)))
        rightBtn.isUserInteractionEnabled = true
        rightBtn.addGestureRecognizer(rightTapRecognizer)
    }
    
    
    //action when left button is pressed is handled here
    @objc func leftImageTapped(leftTapRecognizer: UITapGestureRecognizer)
    {
        let tappedImage = leftTapRecognizer.view?.accessibilityIdentifier
        
        //when accessibility identifier is 2 - cardview is collapsed and infosVisible hidden
        //no carpark selected anymore
        print(tappedImage)
        if tappedImage == "2" {
            
            //Triggered when button shows back
            mapView.deselectAnnotation( mapView.selectedAnnotations.first, animated: false)
        }
    }
    
    
    //action when right button is pressed is handled here
    @objc func rightImageTapped(rightTapRecognizer: UITapGestureRecognizer)
    {
        let tappedImage = rightTapRecognizer.view?.accessibilityIdentifier
        
        //Triggered when button shows target icon
        //current location is shown when access. identifier is 1
        if tappedImage == "1" {
            centerViewOnUserLocation()
        }
        
        //open apple map and copy location of carpark to show there
        if tappedImage == "2" {
            openInMaps(name: currentDestinationName , destCoordinates: currentDestinationCoordinates)
        }
    }
    
    
    //set up cardview with animations
    func setupCard(){
        
        visualEffectView = UIVisualEffectView()
        visualEffectView.frame = self.view.frame
        self.view.addSubview(visualEffectView)
        visualEffectView.isHidden = true
        
        cardViewController = CardViewController(nibName: "CardViewController", bundle: nil)
        self.addChild(cardViewController)
        self.view.addSubview(cardViewController.view)
        
        //set size of cardview
        cardViewController.view.frame = CGRect(x: 0, y: self.view.frame.height - cardHandleAreaHeight, width: self.view.bounds.width, height: cardHeight)
        
        cardViewController.view.clipsToBounds = true
        
        //add gesture recognizer to cardview with animation to open view
        let tapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(ViewController.handleCardTap(recognizer:)))
        
        //add pan gesture with animation
        let panGestureRecognizer = UIPanGestureRecognizer(target: self, action: #selector(ViewController.handleCardPan(recognizer:)))
        
        cardViewController.handleArea.addGestureRecognizer(tapGestureRecognizer)
        cardViewController.handleArea.addGestureRecognizer(panGestureRecognizer)
    }
    
    
    @objc
    func handleCardTap(recognizer: UITapGestureRecognizer) {
        
        switch recognizer.state {
        case .ended:
            animateTransitionIfNeede(state: nextState, duration: 0.9)
        default:
            break
        }
        
    }
    
    @objc
    func handleCardPan(recognizer: UIPanGestureRecognizer){
        
        switch recognizer.state {
        case .began:
            //start transition
            startInteractionTransition(state: nextState, duration: 0.9)
        case .changed:
            //update transition
            let translation = recognizer.translation(in: self.cardViewController.handleArea)
            var fractionComplete = translation.y / cardHeight
            fractionComplete = cardVisible ? fractionComplete : -fractionComplete
            updateInteractionTransition(fractionCompleted: fractionComplete)
        case .ended:
            //continue transition
            continueInteractiveTransition()
        default:
            break
        }
        
    }
    
    func animateTransitionIfNeede(state: CardState, duration: TimeInterval){
        
        if runningAnimations.isEmpty{
            let frameAnimator = UIViewPropertyAnimator(duration: duration, dampingRatio: 1) {
                switch state{
                case .expanded:
                    self.visualEffectView.isHidden = false
                    self.cardViewController.view.frame.origin.y = self.view.frame.height - self.cardHeight
                    
                case .collapsed:
                    self.cardViewController.view.frame.origin.y = self.view.frame.height - self.cardHandleAreaHeight
                }
            }
            frameAnimator.addCompletion{ _ in
                self.cardVisible = !self.cardVisible
                self.runningAnimations.removeAll()
            }
            frameAnimator.startAnimation()
            runningAnimations.append(frameAnimator)
            
            
            let cornerRadiusAnimator = UIViewPropertyAnimator(duration: duration, curve: .linear) {
                switch state{
                case .expanded:
                    self.cardViewController.view.layer.cornerRadius = 12
                case .collapsed:
                    self.cardViewController.view.layer.cornerRadius = 0
                }
            }
            
            cornerRadiusAnimator.startAnimation()
            runningAnimations.append(cornerRadiusAnimator)
            
            let blurrAnimator = UIViewPropertyAnimator(duration: duration, dampingRatio: 1) {
                switch state{
                case .expanded:
                    self.visualEffectView.effect = UIBlurEffect(style: .dark)
                case .collapsed:
                    self.visualEffectView.effect = nil
                }
            }
            
            blurrAnimator.startAnimation()
            runningAnimations.append(blurrAnimator)
            
            blurrAnimator.addCompletion { (_) in
                if state == .collapsed {
                    self.visualEffectView.isHidden = true
                }
            }
        }
    }
    
    func startInteractionTransition(state:CardState, duration:TimeInterval){
        if runningAnimations.isEmpty{
            //start animations
            animateTransitionIfNeede(state: state, duration: duration)
        }
        for animation in runningAnimations {
            animation.pauseAnimation()
            animationProgressWhenInterrupted = animation.fractionComplete
        }
    }
    
    
    func updateInteractionTransition(fractionCompleted:CGFloat){
        
        for animation in runningAnimations {
            animation.fractionComplete = fractionCompleted + animationProgressWhenInterrupted
        }
        
    }
    
    func continueInteractiveTransition(){
        
        for animation in runningAnimations {
            animation.continueAnimation(withTimingParameters: nil, durationFactor: 0)
        }
    }
    
    
    
    //get all carparks with db helper class to set a pin for each carpark on the map
    func fillMap(){
        
        DBHelper.getAllCarParks(completion: {
            
            carParks in
            
            for carPark in carParks {
                
                //unwrapped strings for adress and name
                //convert address from carpark to geo coordinates with AddressConverter
                AddressConverter.convertStringToLatLng(address: carPark.address!, name: carPark.name!, completion: {
                    coordinate, address, name in
                    
                    //set custom pin for coordinates and add to map
                    let pin = CustomPin(pinTitle: name, pinSubTitle: address, location: coordinate)
                    self.mapView.addAnnotation(pin)
                })
                
            }
            
        })
        
    }
    

    //ask user for location permissions to use current location of user
    func checkLocationServices() {
        if CLLocationManager.locationServicesEnabled(){
            print("Location Services enabled")
            setupLocationManager()
            checkLocationAuthorization()
        } else {
            //Show user to turn on location services
            print("Location Services unenabled")
        }
    }
    
    
    //check authorization status of user
    func checkLocationAuthorization(){
        switch CLLocationManager.authorizationStatus() {
        case .authorizedWhenInUse:
            // Do map stuff
            mapView.showsUserLocation = true
            break
        case .denied:
            // Show alert to turn on permissions
            break
        case .notDetermined:
            locationManager.requestWhenInUseAuthorization()
            break
        case .restricted:
            // Show alert that they cant use this app
            break
        case .authorizedAlways:
            break
        }
    }
    
    
    
    //location manager setup
    func setupLocationManager() {
        
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
    }
    
    
    func centerViewOnUserLocation(){
        if let location = locationManager.location?.coordinate {
            let region = MKCoordinateRegion.init(center: location, latitudinalMeters: 10000, longitudinalMeters: 10000)
            mapView.setRegion(region, animated: true)
        }
    }
    
    
    
    
    //change UI images and UI labels according to the status of infosVisible
    func changeNavigation(){
        if infosVisible == false {
            
            navigationLabel.text = "Nearby"
            
            //give all images accessibility identifier for easy handling in other methods - like giving an individual id
            leftBtn.image = UIImage(named: "menu")
            leftBtn.accessibilityIdentifier = "1"
            rightBtn.image = UIImage(named: "gps")
            rightBtn.accessibilityIdentifier = "1"
            
        }
        if infosVisible == true {
            
            navigationLabel.text = "Details"
            
            leftBtn.image = UIImage(named: "back")
            leftBtn.accessibilityIdentifier = "2"
            rightBtn.image = UIImage(named: "navigate")
            rightBtn.accessibilityIdentifier = "2"
            
        }
    }
    
    

    func openInMaps(name: String, destCoordinates: CLLocationCoordinate2D){
        
        let mapItem = MKMapItem(placemark: MKPlacemark(coordinate: destCoordinates, addressDictionary: nil))
        mapItem.name = name
        
        mapItem.openInMaps(launchOptions: [MKLaunchOptionsDirectionsModeKey: MKLaunchOptionsDirectionsModeDriving])
    }
    
    
    
    
    
    
    // ****************** DELEGATES ******************//
    
    //shows pin garage image and description on map
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        if annotation is MKUserLocation { return nil }
        
        let annotationView = MKAnnotationView(annotation: annotation, reuseIdentifier: "MyIdentifier")
        annotationView.image = UIImage(named: "garage")
        annotationView.canShowCallout = true
        annotationView.rightCalloutAccessoryView = UIButton(type: .detailDisclosure) as UIButton
        
        return annotationView
    }
    
    
    //function handles action when carpark is selected
    //show infos on cardview about carpark
    //set cardview visible
    func mapView(_ mapView: MKMapView, didSelect view: MKAnnotationView) {
        print(view.annotation?.title)
        infosVisible = true
        print(infosVisible)
        changeNavigation()
        if infosVisible == true {
            
            
            let navHelper = NavigationHelper()
            
            //calculate destination of user location and carpark location
            navHelper.getRouteToDestination(srcCoordinates: locationManager.location?.coordinate, destCoordinates: view.annotation?.coordinate) { route in
                
                self.currentDestinationCoordinates = view.annotation?.coordinate
                self.currentDestinationName = (view.annotation?.title)!
                
                print(route.distance)
                print(route.polyline)
                
                self.mapView.addOverlay(route.polyline)
                
                let rect = route.polyline.boundingMapRect
                
                self.cardViewController.fillInfoCard(name: (view.annotation?.title! ?? ""), distance: route.distance)
                
                self.mapView.setRegion(MKCoordinateRegion(rect), animated: true)
            }
        }
        
    }
    
    
    //when carpark gets deselected: infos are not visible, cardview is collapsed
    //infos get reseted
    func mapView(_ mapView: MKMapView, didDeselect view: MKAnnotationView) {
        print(view.annotation?.title)
        
        
        self.currentDestinationCoordinates = nil
        self.currentDestinationName = nil
        
        infosVisible = false
        changeNavigation()
        self.cardViewController.resetInfoCard()
        self.mapView.removeOverlay(self.mapView.overlays[0])
    }
    
    
    
    func mapView(_ mapView: MKMapView, annotationView view: MKAnnotationView, calloutAccessoryControlTapped control: UIControl) {
        animateTransitionIfNeede(state: nextState, duration: 0.9)
    }
    
    
    //properties of the polyline are set here: dimension, color, linewidth
    func mapView(_ mapView: MKMapView, rendererFor overlay: MKOverlay) -> MKOverlayRenderer {
        let linerenderer = MKPolylineRenderer(overlay: overlay)
        linerenderer.strokeColor = .blue
        linerenderer.lineWidth = 3.5
        return linerenderer
    }
    
}

