package com.example.asaditasgourmet;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.example.asaditasgourmet.database.DatabaseHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import com.example.asaditasgourmet.R;
import static com.example.asaditasgourmet.R.id.map;


public class UbicacionActivity extends AppCompatActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Object>, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMapClickListener {

    DatabaseHelper db;
    private TextView subtotal;
    private EditText ubic;
    private Button boton;
    RelativeLayout products_select_option;
    ImageButton select_btn, product1, product2;
    private static final String TAG = UbicacionActivity.class.getSimpleName();
    private final static int PERMISSION_MY_LOCATION = 3;
    private static final int REQUEST_CHECK_SETTINGS = 1000;
    private MapFragment mapFragment;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private Location mLastLocation;
    private LocationRequest request;
    View mapView;
    private boolean mRequestingLocationUpdates;
    CameraUpdate cLocation;
    double latitude,longitude;
    Marker now;
    double lat;
    double lng;
    public String path;
    Geocoder geocoder;
    List<android.location.Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);
        setupLocationManager();

        subtotal = (TextView) findViewById(R.id.changeAddressText);
        db = new DatabaseHelper(this);
        subtotal.setText("$ "+db.getSumCart());

        ubic = (EditText) findViewById(R.id.extraInstructionEdiText);
        boton = (Button) findViewById(R.id.payAmountBtn);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SuccessActivity.class);
                intent.putExtra("ubicacion", ubic.getText().toString());
                intent.putExtra("latitud", ""+lat);
                intent.putExtra("longitud", ""+lng);
                startActivity(intent);
            }
        });
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById( R.id.map );
        mapView = mapFragment.getView();
        mapFragment.getMapAsync( this );
    }



    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
        //mGoogleApiClient.connect();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(googleApiClient.isConnected()){
            setInitialLocation();
        }
        LocationManager service = (LocationManager) getSystemService( LOCATION_SERVICE );
        boolean enabled = service.isProviderEnabled( LocationManager.GPS_PROVIDER );
        // Check if enabled and if not send user to the GPS settings
        if (!enabled) {
            buildAlertMessageNoGps();
        }
        if(enabled){
        }
    }



    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style ) );

            if (!success) {
                Log.e( TAG, "Style parsing failed." );
            }
        } catch (Resources.NotFoundException e) {
            Log.e( TAG, "Can't find style. Error: ", e );
        }


        if (ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //This line will show your current location on Map with GPS dot
        mMap.setMyLocationEnabled( true );
        locationButton();
/*
        Toast.makeText( MainActivity.this, "OnStart:"+latitude+","+longitude, Toast.LENGTH_SHORT ).show();
*/
    }


    private void setupLocationManager() {
        //buildGoogleApiClient();
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder( this )
                    .addConnectionCallbacks( this )
                    .addOnConnectionFailedListener( this )
                    .addApi( LocationServices.API )
                    .addApi( Places.GEO_DATA_API )
                    .addApi( Places.PLACE_DETECTION_API )
                    .build();
            //mGoogleApiClient = new GoogleApiClient.Builder(this);
        }
        googleApiClient.connect();
        createLocationRequest();
    }


    protected void createLocationRequest() {
        request = new LocationRequest();
        request.setSmallestDisplacement( 10 );
        request.setFastestInterval( 50000 );
        request.setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY );
        request.setNumUpdates( 3 );

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest( request );
        builder.setAlwaysShow( true );

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings( googleApiClient,
                        builder.build() );


        result.setResultCallback( new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
                        setInitialLocation();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    UbicacionActivity
                                            .this,
                                    REQUEST_CHECK_SETTINGS );
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        } );


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        setInitialLocation();
                        // Toast.makeText(Ubicacion.this, "Location enabled", Toast.LENGTH_LONG).show();
                        mRequestingLocationUpdates = true;
                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        //Toast.makeText(Ubicacion.this, "Location not enabled", Toast.LENGTH_LONG).show();
                        mRequestingLocationUpdates = false;
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }








    private void setInitialLocation() {
        if (ActivityCompat.checkSelfPermission( UbicacionActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( UbicacionActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates( googleApiClient, request, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLastLocation = location;
                lat=location.getLatitude();
                lng=location.getLongitude();

                UbicacionActivity.this.latitude=lat;
                UbicacionActivity.this.longitude=lng;

                try {
                    if(now !=null){
                        now.remove();
                    }
                    LatLng positionUpdate = new LatLng( UbicacionActivity.this.latitude,UbicacionActivity.this.longitude );
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom( positionUpdate, 15 );
                    now=mMap.addMarker(new MarkerOptions().position(positionUpdate)
                            .title("Su Ubicacion"));
                    mMap.animateCamera( update );
                    //myCurrentloc.setText( ""+latitude );
                    //Puntos();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.e( "MapException", ex.getMessage() );
                }
                //Geocode current location details
                try {
                    geocoder = new Geocoder(UbicacionActivity.this, Locale.ENGLISH);
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    StringBuilder str = new StringBuilder();
                    if (Geocoder.isPresent()) {
                        android.location.Address returnAddress = addresses.get(0);
                        String localityString = returnAddress.getAddressLine (0);
                        str.append( localityString ).append( "" );
                        ubic.setText(str);
                    } else {
                    }
                } catch (IOException e) {
                    Log.e("tag", e.getMessage());
                }
            }
        });
    }

    private void updateCamera(){

    }



    public void getLatLang(String placeId) {
        Places.GeoDataApi.getPlaceById( googleApiClient, placeId )
                .setResultCallback( new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess() && places.getCount() > 0) {
                            final Place place = places.get( 0 );
                            LatLng latLng = place.getLatLng();
                            try {
                                CameraUpdate update = CameraUpdateFactory.newLatLngZoom( latLng, 15 );
                                mMap.animateCamera( update );
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Log.e( "MapException", ex.getMessage() );
                            }
                            Log.i( "place", "Place found: " + place.getName() );
                        } else {
                            Log.e( "place", "Place not found" );
                        }
                        places.release();
                    }
                } );
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //AlertMessageNoGps();
    }


    @Override
    public void onConnectionSuspended(int i) {
        //checkLocaionStatus();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {


    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public Loader<Object> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object o) {

    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }


    //GET CURRENT LOCATION BUTTON POSITION....
    private void locationButton() {
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById( map );

        View locationButton = ((View) mapFragment.getView().findViewById( Integer.parseInt( "1" ) ).
                getParent()).findViewById( Integer.parseInt( "2" ) );
        if (locationButton != null && locationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

            // Align it to - parent BOTTOM|LEFT
            params.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM );
            params.addRule( RelativeLayout.ALIGN_PARENT_LEFT );
            params.addRule( RelativeLayout.ALIGN_PARENT_RIGHT, 0 );
            params.addRule( RelativeLayout.ALIGN_PARENT_TOP, 0 );

            // Update margins, set to 10dp
            final int margin = (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 150,
                    getResources().getDisplayMetrics() );
            params.setMargins( margin, margin, margin, margin );
            locationButton.setLayoutParams( params );
        }
    }


    //Button Location Search
    public void myLocation(View view) {
    }


    public void destination(View view) {
    }


    //Select product option button click
    public void img_selected(View view) {
        products_select_option.setVisibility( View.VISIBLE );
    }

    //Select product option button click
    public void product_type_1_button(View view) {
        products_select_option.setVisibility( View.GONE );

    }

    //Select product option button click
    public void product_type_2_button(View view) {
        products_select_option.setVisibility( View.GONE );

    }

    //Enable Location Button
    private void checkLocaionStatus() {


    }



    protected void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setTitle( "GPS Not Enabled" )
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    //Method for Writing data in txt files
    public static void SaveRecord(File file, String saveRecord) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            try {
                fileOutputStream.write(saveRecord.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(getApplicationContext(), ""+latLng.latitude+" /  "+latLng.longitude, Toast.LENGTH_LONG).show();
    }
}
