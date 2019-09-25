package com.hdm.bonoboparking.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.hdm.bonoboparking.utils.AddressConverter;
import com.hdm.bonoboparking.database.DataSource;
import com.hdm.bonoboparking.R;
import com.hdm.bonoboparking.database.InsertParkingLot;
import com.hdm.bonoboparking.database.ParkingLot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {


    //Constants for logging
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String PERMISSION_LOG_TAG = "Permission";
    public static final String LOCATION_LOG_TAG = "Location_Testing";
    private static final String TOUCH_EVENT = "Touch Event";

    /*+++++++++++++ Constants for permissions +++++++++++++*/
    static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    static final String INTERNET_ACCESS = Manifest.permission.INTERNET;
    private static final String[] PERMISSIONS = {FINE_LOCATION, COARSE_LOCATION, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, INTERNET_ACCESS};
    private static final int ALL_REQUEST_CODE = 9000;
    private static boolean permissionsGranted = false;

    /*+++++++++++++ Variables for UI +++++++++++++*/
    private BottomSheetBehavior mBottomSheetBehavior;
    private CoordinatorLayout coordinatorLayout;
    private View bottomSheet;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private ImageButton leftToolbarBtn; //Either burger menu or arrow
    private ImageButton rightToolbarBtn; //Either location cross or navigation arrow
    private TextView toolbarText; //Either details or nearby
    private TextView carParkName;
    private TextView totalPlaces;
    private TextView addressField;
    private TextView hoursField;
    private TextView distanceField;
    private TextView tariffField;

    boolean bottomSheetShowsDetails = false;

    private ImageView disabledIcon;
    private ImageView guardIcon;
    private ImageView roofIcon;
    private ImageView femaleIcon;
    private ImageView familyIcon;
    private ImageView carIcon;


    SharedPreferences preferences;

    /*+++++++++++++ Variables for map +++++++++++++*/
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private GoogleMap mMap;
    boolean followPosition = false;
    boolean mapReady = false;
    private MapView mapView;
    private Bundle mapViewBundle;
    private Polyline currentPolyline;

    /*+++++++++++++ Variables for Database related things +++++++++++++*/
    private DataSource dataSource;
    private HashMap<String, ParkingLot> carParksMap;
    /*+++++++++++++ Variables for location related things +++++++++++++*/
    final int REQUEST_CHECK_SETTINGS = 1;
    final int REQUEST_LOCATION = 2;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private SettingsClient mSettingsClient;
    private LocationCallback mLocationCallback;
    private LocationSettingsRequest mLocationSettingsRequest;
    private Location mCurrentLocation;
    public Boolean locUpdates;
    public Boolean useGPS;
    private LatLng coordinate;
    private AddressConverter addressConverter;
    private GeoApiContext mGeoApiContext;



    @SuppressLint({"MissingPermission", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*+++++++++++++ Initializing all layout components +++++++++++++*/

        mapView = findViewById(R.id.mapView);
        bottomSheet = findViewById(R.id.bottom_sheet);
        coordinatorLayout = findViewById(R.id.coordinateLayout);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        carParkName = findViewById(R.id.carParkName);
        totalPlaces = findViewById(R.id.totalSpaces);
        addressField = findViewById(R.id.addressField);
        hoursField = findViewById(R.id.hoursField);
        tariffField = findViewById(R.id.tariffField);
        distanceField = findViewById(R.id.distanceText);
        leftToolbarBtn = findViewById(R.id.leftToolbarBtn);
        rightToolbarBtn = findViewById(R.id.rightToolbarBtn);
        toolbarText = findViewById(R.id.toolbarText);
        mDrawerList = findViewById(R.id.navList);
        mDrawerLayout = findViewById(R.id.drawer_layout);


        disabledIcon = findViewById(R.id.disabledIcon);
        guardIcon = findViewById(R.id.guardIcon);
        roofIcon = findViewById(R.id.roofIcon);
        femaleIcon = findViewById(R.id.femaleIcon);
        familyIcon = findViewById(R.id.familyIcon);
        carIcon = findViewById(R.id.carIcon);


        //PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        /*+++++++++++++ Loads all side menu components (Carfinder & Settings) +++++++++++++*/
        addDrawerItems();
        /*+++++++++++++ Sets the standard toolbar visible +++++++++++++*/
        setToolbarStandard();
        /*+++++++++++++ Initializing Bottom Sheet (shows no specific information) +++++++++++++*/
        initBottomSheet();

        useGPS = false;
        locUpdates = false;

        /*+++++++++++++ Initializing the FusedLocationClient and SettingsClient for updating to location information +++++++++++++*/
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        /*+++++++++++++ Creates Location Callback and Request for continuous location updates +++++++++++++*/
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

        /*+++++++++++++ Initializing of data source object to access database +++++++++++++*/
        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird angelegt.");
        dataSource = new DataSource(getApplicationContext());
        dataSource.open();

        /*+++++++++++++ Initializes new AddressConverter to convert from address to latlong data +++++++++++++*/
        addressConverter = new AddressConverter();


        /*+++++++++++++ Inserting Test Data to DB with class InsertParkLot +++++++++++++*/
        InsertParkingLot insertParkingLot = new InsertParkingLot();
        insertParkingLot.insertInTable(getApplicationContext());


        /*+++++++++++++ On click listener for side menu components +++++++++++++*/
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //ID counts and identifies from top to bottom which item is clicked. Starting from 0
                if (id == 0) {
                    //CarFinder is clicked
                } else if (id == 1) {
                    //Settings is clicked
                }

            }
        });

        /*+++++++++++++ OnClickListener to open BottomSheet when clicked +++++++++++++*/
        bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        /*+++++++++++++ OnClickListener for right toolbar buttons +++++++++++++*/
        rightToolbarBtn.setOnClickListener(v -> {

            if (rightToolbarBtn.getId() == 1) {
                //right toolbar button was clicked in standard mode
                if (mapReady) {
                    followPosition = true;
                    updateCamera();
                }

            } else if (rightToolbarBtn.getId() == 2) {
                //right toolbar button was clicked in details mode

            }
        });

        /*+++++++++++++ OnClickListener for left toolbar buttons +++++++++++++*/
        leftToolbarBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (leftToolbarBtn.getId() == 1) {
                    //left toolbar button was clicked in standard mode

                    mDrawerLayout.openDrawer(Gravity.LEFT);
                } else if (leftToolbarBtn.getId() == 2) {
                    //left toolbar button was clicked in details mode
                    //Minimizes the bottomsheet and sets default look
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    initBottomSheet();
                }
            }
        });

        /*+++++++++++++ Changelistener for bottomsheet +++++++++++++*/
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                //sets details toolbar if bottomsheet is expanded
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                    setToolbarDetails();

                    //sets standard toolbar if bottomsheet is collapsed
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {

                    setToolbarStandard();
                    initBottomSheet();
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }


        });


        /*+++++++++++++ Calls method to check permissions +++++++++++++*/
        checkPermissions(getApplicationContext(), PERMISSIONS);


        //Sets mapViewBundle = null and loads a saved instance if existing
        mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

        Log.d(LOCATION_LOG_TAG, "On Resume called");
        int googlePlayAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if (googlePlayAvailable != ConnectionResult.SUCCESS) {
            Toast toast = Toast.makeText(this, "Make sure the newest Google Play Services version is installed", Toast.LENGTH_LONG);
            toast.show();
        }

        if (useGPS && !locUpdates) {
            checkPermissions(this, PERMISSIONS);

        }

        //open datasource
        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();

    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
        if (locUpdates) {
            stopLocationUpdates();
        }

        //close datasource
        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();

    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    private void initMap() {

        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder().apiKey(getString(R.string.google_maps_key)).build();
        }

    }

    //TODO Ask if Surpress Lint is okay in this case
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        /*+++++++++++++ Initializes Map +++++++++++++*/
        mMap = googleMap;
        //Makes own position visible
        mMap.setMyLocationEnabled(true);
        //Disables the show my location button
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        //Sets map onclicklistener
        mMap.setOnMyLocationClickListener(this);
        //Sets marker onclicklistener
        mMap.setOnMarkerClickListener(this);
        //Disables map toolbar
        mMap.getUiSettings().setMapToolbarEnabled(false);
        //sets followPosition to false to not longer zoom to users location
        mMap.setOnMapClickListener(latLng -> followPosition = false);
        //Calls method to load all carparks and add them as markers
        generateCarParks();


    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        // Gets called when user clicks on his position
    }

    /*+++++++++++++ checks if all permissions are granted +++++++++++++*/
    private void checkPermissions(Context context, String[] permissions) {

        Log.d(PERMISSION_LOG_TAG, "Asking for permissions");

     new Thread(() -> {

         if (context != null && permissions != null) {

             //Iterates over every object in PERMISSIONS array
             for (String permission : permissions) {

                 //Checks if the permission is granted, if not returns false
                 if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                     //Permission is not granted
                     //Asking for permission
                     ActivityCompat.requestPermissions(this, PERMISSIONS, ALL_REQUEST_CODE);
                 } else {
                     initMap();
                     startLocationUpdates();
                 }
             }
         }
     }).run();
    }


    /*+++++++++++++ Callback function for permissions +++++++++++++*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        permissionsGranted = false;

        //Gets request code (ALL_REQUEST_CODE)
        switch (requestCode) {
            //If requestCode equals our ALL_REQUEST_CODE
            case ALL_REQUEST_CODE: {
                //IF the array of grantResults is bigger than 0
                if (grantResults.length > 0) {
                    //Iterates over every item in array
                    for (int grantResult : grantResults) {
                        //If an permission is missing it returns
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }
                    }

                    //If everything is fine, location updates are started and the map gets initialized
                    permissionsGranted = true;
                    Log.d(LOCATION_LOG_TAG, "onRequestPermissionResult called");
                    startLocationUpdates();
                    initMap();
                }
            }
        }
    }

    /*+++++++++++++ Method to inizialize Standard and Details Toolbar +++++++++++++*/
    @SuppressLint("ResourceType")
    public void setToolbarStandard() {
        leftToolbarBtn.setImageResource(R.drawable.menu);
        toolbarText.setText(R.string.standardText);
        rightToolbarBtn.setImageResource(R.drawable.my_location);
        //1 represents the button being in the standard toolbar
        leftToolbarBtn.setId(1);
        rightToolbarBtn.setId(1);

    }

    @SuppressLint("ResourceType")
    public void setToolbarDetails() {
        leftToolbarBtn.setImageResource(R.drawable.back_arrow);
        toolbarText.setText((R.string.details));
        rightToolbarBtn.setImageResource(R.drawable.start_navigation);
        //2 represents the button being in the details toolbar
        leftToolbarBtn.setId(2);
        rightToolbarBtn.setId(2);
    }

    /*+++++++++++++ Method to load side menu items in the side menu  +++++++++++++*/
    private void addDrawerItems() {
        String[] menuArray = {"Car Finder", "Settings"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuArray);
        mDrawerList.setAdapter(mAdapter);
    }


    /*+++++++++++++ Location Methods +++++++++++++*/

    //Creating the request and sets request parameter (like how often to update location etc.)
    protected void createLocationRequest() {
        Log.d(LOCATION_LOG_TAG, "startLocationRequest called");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);  // preferred update rate
        mLocationRequest.setFastestInterval(1000);  // fastest rate app can handle updates
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    //Get current locations settings of user's device
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
        Log.d(LOCATION_LOG_TAG, "buildLocationSettingsRequest called");

    }

    /*+++++++++++++ Starts location updates +++++++++++++*/
    private void startLocationUpdates() {
        Log.d(LOCATION_LOG_TAG, "startLocationUpdates called");
        // if settings are satisfying initialize location requests
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest).addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @SuppressLint("MissingPermission")
            @Override
            //Gets called if settings are good. Starts updating the location as specified in the Request
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                locUpdates = true;
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }
        })
                // Gets called if settings are bad or wrong
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // location settings are not satisfied, but this can be fixed by showing the user a dialog.
                                try {
                                    // show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                                    ResolvableApiException resolvable = (ResolvableApiException) e;
                                    resolvable.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sendEx) {
                                    // Ignore the error
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // location settings are not satisfied, however no way to fix the settings so don't show dialog.
                                Toast.makeText(MainActivity.this, "Location Services Unavailable", Toast.LENGTH_LONG).show();
                                useGPS = false;
                                SharedPreferences.Editor edit = preferences.edit();
                                edit.putBoolean("use_device_location", false);
                                edit.apply();
                                break;
                        }
                    }
                });
    }


    // Get results from user dialog prompt to turn on location services for app
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                        } else {
                            // Start location updates
                            // Note - in emulator location appears to be null if no other app is using GPS at time.
                            // So if just turning on device's location services getLastLocation will likely not return anything
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                            locUpdates = true;
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        // user does not want to update setting.
                        useGPS = false;
                        break;
                }
                break;
        }
    }

    // stop location updates
    private void stopLocationUpdates() {
        locUpdates = false;
        mFusedLocationClient.removeLocationUpdates(mLocationCallback).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }

    private void createLocationCallback() {
        Log.d(LOCATION_LOG_TAG, "createLocationCallback called");
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                setLatLong(mCurrentLocation);
                Log.d(LOCATION_LOG_TAG, "onLocationResult called");

                coordinate = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                mapReady = true;

            }
        };
    }

    // Set new latitude and longitude based on location results
    public void setLatLong(Location location) {
        double lastLat = location.getLatitude();
        double lastLong = location.getLongitude();
        mCurrentLocation = location;
        Log.d(LOCATION_LOG_TAG, "setLatLOng called");
        System.out.println(lastLat + " " + lastLong);


    }

    //update camera when user has new location
    protected void updateCamera() {
        if (followPosition) {
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinate, 13);
            mMap.animateCamera(location);


        }


    }


    //collapse Bottomsheet when clicking outside of it
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d(TOUCH_EVENT, "dispatchTouchEventCalled");
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

                Rect outRect = new Rect();
                bottomSheet.getGlobalVisibleRect(outRect);

                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    System.out.println("Second if called");
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    initBottomSheet();
                    removePolyline(currentPolyline);
                }


            }
        }

        return super.dispatchTouchEvent(event);
    }


    // generate a Hash Map of all carparks from the table to set a marker for each carpark
    protected void generateCarParks() {

        //Opens new thread to collect all car parks
        new Thread(() -> {
            carParksMap = dataSource.getAllParkingSpacesAsMap();
            //Iterates over every car park object and sets marker
            for (Map.Entry<String, ParkingLot> entry : carParksMap.entrySet()) {
                ParkingLot park = entry.getValue();
                //Calls addressConverter )
                LatLng coordinates = addressConverter.getLocationFromAddress(getApplicationContext(), park.getAdress());
                park.setLatLng(coordinates);
                String parkName = park.getName();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mMap != null && coordinates != null) {
                            mMap.addMarker(new MarkerOptions().position(coordinates).title(parkName));
                        }
                    }
                });

            }
        }).start();


    }

    //Gets called if a marker gets clicked, gets also the clicked marker
    @Override
    public boolean onMarkerClick(Marker marker) {
        //Logs Name of carPark
        Log.d(LOCATION_LOG_TAG, "Marker of " + marker.getTitle() + " clicked");
        //Calls method to open all details of the clicked marker/carPark
        openInfoSheetForMarker(marker);
        calculateDirections(marker);
        calculateDistance(marker);

        return false;
    }

    //show data from table on bottom sheet the selected carpark
    public void openInfoSheetForMarker(Marker marker){
        String markerName = marker.getTitle();

        ParkingLot tempCarPark = carParksMap.get(markerName);


        if (tempCarPark.getName() != null) {

            Log.d("Parkhaus", "Parkhaus ist " + tempCarPark.getName());


            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            carParkName.setText(tempCarPark.getName());
            addressField.setText(tempCarPark.getAdress());
            hoursField.setText(tempCarPark.getOpenHours());
            tariffField.setText(tempCarPark.getTariff());
            totalPlaces.setText(Integer.toString(tempCarPark.getTotalPlaces()));
            carIcon.setVisibility(View.VISIBLE);

            setIcons(tempCarPark);

            bottomSheetShowsDetails = true;
        }
    }


    //set icons according to the selected carpark, not every carpark has same properties
    private void setIcons(ParkingLot carPark) {

        if(carPark != null) {

            if(carPark.isDisabledParking() != null){
                disabledIcon.setVisibility(View.VISIBLE);
            }
            if (carPark.isFamilyParking() != null){
                familyIcon.setVisibility(View.VISIBLE);
            }
            if(carPark.isIndoorParking() != null){
                roofIcon.setVisibility(View.VISIBLE);
            }
            if (carPark.isSecurity() != null){
                guardIcon.setVisibility(View.VISIBLE);
            }
            if (carPark.isWomenParking() != null){
                femaleIcon.setVisibility(View.VISIBLE);
            }
        }

    }


    //Sets default look of bottom sheet
    public void initBottomSheet() {
        bottomSheetShowsDetails = false;
        carParkName.setText(R.string.initialBottomSheet);
        addressField.setText("");
        hoursField.setText("");
        tariffField.setText("");
        totalPlaces.setText("");
        distanceField.setText("");

        carIcon.setVisibility(View.GONE);
        disabledIcon.setVisibility(View.GONE);
        familyIcon.setVisibility(View.GONE);
        femaleIcon.setVisibility(View.GONE);
        guardIcon.setVisibility(View.GONE);
        roofIcon.setVisibility(View.GONE);
    }

    //ask if it is okay to use the Maps API to draw polylines
    //show polyline on map to see distance between personal location and carpark
    private void buildPolylines(final DirectionsResult result) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(LOCATION_LOG_TAG, "run: result routes: " + result.routes.length);

                for (DirectionsRoute route : result.routes) {
                    Log.d(LOCATION_LOG_TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for (com.google.maps.model.LatLng latLng : decodedPath) {


                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }


                    if (currentPolyline != null) {

                        removePolyline(currentPolyline);

                    }
                    currentPolyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    // polyline.setColor(ContextCompat.getColor(getActivity(), R.color.darkGrey));
                    currentPolyline.setClickable(true);


                }
            }
        });

    }


    //calculate direction between marker and location of user to show polyline
    private void calculateDirections(Marker marker) {


        Log.d(LOCATION_LOG_TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                marker.getPosition().latitude,
                marker.getPosition().longitude
        );

        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        // directions.alternatives(true);
        directions.origin(
                new com.google.maps.model.LatLng(

                        mCurrentLocation.getLatitude(),
                        mCurrentLocation.getLongitude()
                )
        );
        Log.d(LOCATION_LOG_TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(LOCATION_LOG_TAG, "onResult: routes: " + result.routes[0].toString());
                Log.d(LOCATION_LOG_TAG, "onResult: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());

                buildPolylines(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(LOCATION_LOG_TAG, "onFailure: " + e.getMessage());

            }
        });
    }


    //remove polyline when unnecessary
    void removePolyline(Polyline polyline) {
        if(polyline != null){
            polyline.remove();
        }

    }

    void calculateDistance(Marker marker) {
        DistanceMatrixApiRequest distanceMatrixApiRequest = DistanceMatrixApi.newRequest(mGeoApiContext);

        com.google.maps.model.LatLng origin = new com.google.maps.model.LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(marker.getPosition().latitude, marker.getPosition().longitude);

        distanceMatrixApiRequest.origins(origin).destinations(destination).mode(TravelMode.DRIVING);

        distanceMatrixApiRequest.setCallback(new PendingResult.Callback<DistanceMatrix>() {
            @Override
            public void onResult(DistanceMatrix result) {
                Log.d(LOCATION_LOG_TAG, "The Result for distance is: " + result.rows[0].elements[0].distance.humanReadable);
                String distanceString = result.rows[0].elements[0].distance.humanReadable;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        distanceField.setText(distanceString);
                    }
                });

            }

            @Override
            public void onFailure(Throwable e) {

                Log.e(LOCATION_LOG_TAG, "onFailure calculate distance: " + e.getMessage());
            }
        });

    }
}

