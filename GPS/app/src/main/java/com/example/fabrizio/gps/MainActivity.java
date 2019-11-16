package com.example.fabrizio.gps;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import android.widget.ListView;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import butterknife.BuildConfig;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    boolean google;
    boolean nat;
    boolean g;
    boolean n;
    boolean p1;
    boolean p2;
    boolean ground;
    boolean inizio;
    int check;
    int landmark;
    int turn;
    int final_method;
    File folder;
    File file;
    String[] gt = new String[5];
    String[] go = new String[5];
    String[] nt = new String[5];
    String[] pf = new String[5];
    String[] pf_out = new String[5];
    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HHmmss");
    final SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    //variabili native gps
    private LocationManager locationManager;
    private Criteria criteria;
    private String provider;
    private LocationListener mylistener;
    GeoPoint nowN;
    ArrayList<Marker> lstartMarkerN = new ArrayList<>();

    final boolean[] checked = {false, false, false, false};
    private static final String TAG = MainActivity.class.getSimpleName();

    //variabili googgle gps
    MapView map = null;
    GeoPoint now;
    IMapController mapController;
    ArrayList<Marker> lstartMarker = new ArrayList<>();

    //variabili pf gps
    GeoPoint nowPf;
    ArrayList<Marker> lstartMarkerPf = new ArrayList<>();

    // variabili groudtruth e varie interfacce
    Button btnground;
    AlertDialog.Builder builder;
    AlertDialog.Builder builder1;
    AlertDialog dialog;
    Toolbar myToolbar;
    MenuItem start;
    MenuItem stop;
    // location updates interval - 5sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 2000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;

    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;




    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //permesso per accesso ai contenuti multimediali
        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == getPackageManager().PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
        //permesso per accedere alla posizione del dispositivo
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnground = findViewById(R.id.ground);
        btnground.setVisibility(View.INVISIBLE);
        builder = new AlertDialog.Builder(MainActivity.this);
        builder1 = new AlertDialog.Builder(MainActivity.this);
        google = false;
        ground = false;
        nat = false;
        n = false;
        g = false;
        p1 = false;
        p2 = false;
        inizio = true;
        check = 0;
        landmark = 0;
        turn = 2;

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackgroundColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // map options
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.CYCLEMAP);
        mapController = map.getController();
        mapController.setZoom(18);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        ButterKnife.bind(this);

        // initialize the necessary libraries
        init();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED) {
                    //reload my activity with permission granted or use the features what required the permission

                    finish();
                    startActivity(getIntent());
                } else {
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    //metodo di inizio della geolocalizzazione
    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                String time = "";
                //calcolo il tempo per google e pf
                if(g || p1) {
                    long time1=System.currentTimeMillis();
                    time = format1.format(time1);

                }
                //groundtruth
                if(ground) {
                    btnground.setVisibility(View.VISIBLE);
                    btnground.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(MainActivity.this, "New Landmark inserted", Toast.LENGTH_SHORT).show();
                            long time1 = System.currentTimeMillis();
                            String time = format1.format(time1);
                            gt[0] = "Groundtruth;";
                            gt[1] = time + ";";
                            try {
                                FileWriter writer = new FileWriter(file, true);
                                gt[2] = "gt_lat_" + landmark + ";";
                                gt[3] = "gt_long_" + landmark + ";";
                                gt[4] = "gt_alt_" + landmark + "\n";
                                for (String str : gt) {
                                    writer.write(str);
                                }
                                writer.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            landmark++;
                            btnground.setText("LANDMARK" + landmark);

                        }
                    });
                }
                if (mCurrentLocation != null) {

                    final double latitude = mCurrentLocation.getLatitude();
                    final double longitude =  mCurrentLocation.getLongitude();
                    final double altitude =  mCurrentLocation.getAltitude();

                    String lat =  mCurrentLocation.convert(latitude,  mCurrentLocation.FORMAT_DEGREES );
                    String lon =  mCurrentLocation.convert(longitude,  mCurrentLocation.FORMAT_DEGREES );
                    //posiziono la mappa sul punto trovato se non sono all'inizio e se non sono attivi google e nativo
                    if(!g && !inizio && !n) {
                        now = new GeoPoint(latitude, longitude, altitude);
                        mapController.setCenter(now);
                    }
                    //google
                    if(g) {
                        go[0] = "Google;";
                        go[1] = time + ";";
                        go[2] = lat + ";";
                        go[3] = lon + ";";
                        if (altitude <= 0.0 || altitude >= 10000)
                            go[4] = "not found\n";
                        else
                            go[4] = altitude + "\n";
                        try {
                            FileWriter writer = new FileWriter(file, true);
                            for (String str : go) {
                                writer.write(str);
                            }
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Executors.newCachedThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                plotOnMap(latitude, longitude, altitude, 1);
                            }

                        });
                    }
                    //particle filtering send
                    if (p1) {
                        pf[0] = "ParticleFilter_Google_Send;";
                        pf[1] = time + ";";
                        pf[2] = lat + ";";
                        pf[3] = lon + ";";
                        if (altitude <= 0.0 || altitude >= 10000)
                            pf[4] = "not found\n";
                        else
                            pf[4] = altitude + "\n";
                        String alt = Double.toString(altitude);
                        try {
                            FileWriter writer = new FileWriter(file, true);
                            for (String str : pf) {
                                writer.write(str);
                            }
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        callThread(latitude, longitude, altitude);;
                    }

                }
            }
        };

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        stop = menu.findItem(R.id.stop);
        stop.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        start = menu.findItem(R.id.inizio);
        stop = menu.findItem(R.id.stop);
        if (turn == 0) {
            start.setVisible(false);
            stop.setVisible(true);
        }
        else if(turn == 1){
            start.setVisible(true);
            stop.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void googleAct() {
        Task<LocationSettingsResponse> locationSettingsResponseTask = mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");
                        init();
                        //Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());


                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }


                    }
                });

    }

    public void nativeAct() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);   //default

        criteria.setCostAllowed(false);
        // get the best provider depending on the criteria
        provider = locationManager.getBestProvider(criteria, false);
        // the last known location of this provider
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return true;

        }
        Location location = locationManager.getLastKnownLocation(provider);
            mylistener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    long time1=System.currentTimeMillis();
                    String time = format1.format(time1);
                    final double latitude = location.getLatitude();
                    final double longitude = location.getLongitude();
                    final double altitude = location.getAltitude();
                    final String lat = location.convert(latitude, location.FORMAT_DEGREES);
                    final String lon = location.convert(longitude, location.FORMAT_DEGREES);
                    //se non sono attivi google e nativo mi posiziono sul punto calcolato
                    if(!g  && !n) {
                        now = new GeoPoint(latitude, longitude, altitude);
                        mapController.setCenter(now);
                    }
                    //nativo
                    if(n) {
                        nt[0] = "Native;";
                        nt[1] = time + ";";
                        nt[2] = lat + ";";
                        nt[3] = lon + ";";
                        if (altitude <= 0.0 || altitude >= 10000.0)
                            nt[4] = "not found\n";
                        else
                            nt[4] = altitude + "\n";
                        try {
                            FileWriter writer = new FileWriter(file, true);
                            for (String str : nt) {
                                writer.write(str);
                            }
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                plotOnMap(latitude, longitude, altitude, 2);
                            }
                        });

                    }
                    //particle filtering send
                    if(p2){
                        pf[0] = "ParticleFilter_Native_Send;";
                        pf[1] = time + ";";
                        pf[2] = lat + ";";
                        pf[3] = lon + ";";
                        if (altitude == 0.0 || altitude >= 10000)
                            pf[4] = "not found\n";
                        else
                            pf[4] = altitude + "\n";
                        String alt = Double.toString(altitude);
                        try {
                            FileWriter writer = new FileWriter(file, true);
                            for (String str : pf) {
                                writer.write(str);
                            }
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        callThread(latitude, longitude, altitude);
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    // Toast.makeText(MainActivity.this, provider + "'s status changed to "+status +"!",
                    // Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Toast.makeText(MainActivity.this, "Provider " + provider + " enabled!",
                            Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onProviderDisabled(String provider) {
                    Toast.makeText(MainActivity.this, "Provider " + provider + " disabled!",
                            Toast.LENGTH_SHORT).show();

                }
            };
        if (location != null) {
            mylistener.onLocationChanged(location);

        }
        else {
            // leads to the settings because there is no last known location
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return true;
        }
        // location updates: at least 1 meter and 200millsecs change
        locationManager.requestLocationUpdates(provider, 5000, 1, mylistener);
        //return true;
    }

    //particle filtering recv
    public void callThread(Double lat, Double lon, Double alt) {
        ClientServer client = new ClientServer(lat, lon, alt);
        Thread thread = new Thread(client);
        thread.start();
        try {
            thread.join(5000);
            String[] response = client.getValue().split(";");
            long time1 = System.currentTimeMillis();
            pf_out[1] = format1.format(time1) + ";";
            final Double latitude = Double.parseDouble(response[0]);
            final Double longitude = Double.parseDouble(response[1]);
            final Double altitude = Double.parseDouble(response[2]);
            pf_out[2] =  mCurrentLocation.convert(latitude,  mCurrentLocation.FORMAT_DEGREES ) + ";";
            pf_out[3] =  mCurrentLocation.convert(longitude,  mCurrentLocation.FORMAT_DEGREES ) + ";";
            if (p1)
                pf_out[0] = "ParticleFilter_Google_Recv;";
            else
                pf_out[0]  = "ParticleFilter_Native_Recv;";
            if(altitude <= 0.0 || altitude >= 10000)
                pf_out[4] = "not found\n";
            else
                pf_out[4] = altitude + "\n";
            try {
                FileWriter writer = new FileWriter(file, true);
                for (String str : pf_out) {
                    writer.write(str);
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Executors.newCachedThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    plotOnMap(latitude, longitude, altitude, 3);
                }

            });
        }
        catch(Exception e) {
            Toast.makeText(MainActivity.this, "Server Inattivo", Toast.LENGTH_SHORT).show();
        }
    }


    public void clearMap() {

        if (!lstartMarker.isEmpty()) {
            for (int i = 0; i < lstartMarker.size(); i++) {
                map.getOverlays().remove(lstartMarker.get(i));
            }
            lstartMarker.clear();
        }
        if (!lstartMarkerN.isEmpty()) {
            for (int i = 0; i < lstartMarkerN.size(); i++) {
                map.getOverlays().remove(lstartMarkerN.get(i));
            }
            lstartMarkerN.clear();
        }
        if (!lstartMarkerPf.isEmpty()) {
            for (int i = 0; i < lstartMarkerPf.size(); i++) {
                map.getOverlays().remove(lstartMarkerPf.get(i));
            }
            lstartMarkerPf.clear();
        }
        map.invalidate();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {

            //quando premo start
            case R.id.inizio:
                AlertBinary();
                break;
            //quando premo stop
            case R.id.stop:
                // Removing location updates
                if (mFusedLocationClient != null) {
                    mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                }

                mFusedLocationClient = null;
                mLocationRequest = null;
                mLocationCallback = null;
                if(locationManager!=null)
                {
                    locationManager.removeUpdates(mylistener);
                    locationManager = null;
                }
                landmark = 0;
                btnground.setVisibility(View.INVISIBLE);
                btnground.setText("LANDMARK 0");
                google = false;
                nat = false;
                ground = false;
                n = false;
                g = false;
                p1 = false;
                p2 = false;
                turn = 1;
                invalidateOptionsMenu();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public void AlertBinary() {

        builder.setTitle("Choose the methods");
        final String[] options = {"Groundtruth", "Google", "Native", "Particle Filter"};
        builder.setMultiChoiceItems(options, checked,
                new DialogInterface.OnMultiChoiceClickListener() // Item click listener
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                        checked[i] = isChecked;
                        /*String currentItem = options[i];

                        Toast.makeText(getApplicationContext(),
                                currentItem + " " + isChecked, Toast.LENGTH_SHORT).show();
                                */
                    }
                });
        // Add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        //quando premo cancel sulla finestra di scelta delle tecniche
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                turn = 1;
                invalidateOptionsMenu();
            }
        });

        dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        //quando premo ok sulla finestra di scelta delle tecniche
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checked[0] && !checked[1] && !checked[2] && !checked[3]) {
                    Toast.makeText(MainActivity.this, "Select at least one method", Toast.LENGTH_SHORT).show();
                } else {
                    inizio = false;
                    turn = 0;
                    invalidateOptionsMenu();
                    for (int i = 0; i < options.length; i++) {
                        if (checked[i])
                            final_method = i;
                    }
                    if(final_method < 3) {
                        String name = "/rilevamenti_geocomp";
                        folder = new File(Environment.getExternalStorageDirectory(), name);
                        if (!folder.exists()) {
                            folder.mkdirs();
                        }
                        long time = System.currentTimeMillis();

                        String data = format.format(time);

                        file = new File(folder + "/" + data + ".csv");
                        try {
                            FileWriter writer = new FileWriter(file, true);
                            String entries[] = {"Type;", "Time;", "Latitude;", "Longitude;", "Altitude\n"};
                            for (String str : entries) {
                                writer.write(str);
                            }
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        clearMap();
                    }

                    for (int i = 0; i < options.length; i++) {
                        if (checked[i]) {
                            switch (i) {
                                case 0:
                                    if (landmark == 0) {
                                        ground = true;
                                        google = true;
                                    }
                                    if (final_method == 0) {
                                        if (google)
                                            googleAct();
                                        if (nat)
                                            nativeAct();
                                    }
                                    break;
                                case 1:
                                    google = true;
                                    g = true;
                                    if (final_method == 1) {
                                        if (google)
                                            googleAct();
                                        if (nat)
                                            nativeAct();
                                    }
                                    break;
                                case 2:
                                    nat = true;
                                    n = true;
                                    if (final_method == 2) {
                                        if (google)
                                            googleAct();
                                        if (nat)
                                            nativeAct();
                                    }
                                    break;
                                case 3:
                                    builder1.setTitle("Choose the basic method for the Particle filter");
                                    final String[] options1 = {"Google", "Native"};
                                    builder1.setSingleChoiceItems(options1, -1,
                                            new DialogInterface.OnClickListener() // Item click listener
                                            {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int which) {
                                                    ListView lv = (dialog).getListView();
                                                    lv.setTag(new Integer(which));
                                                }
                                            });
                                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                                    final AlertDialog dialog1 = builder1.create();
                                    dialog1.show();
                                    dialog1.setCancelable(false);
                                    dialog1.setCanceledOnTouchOutside(false);
                                    //quando premo ok nella finestra di scelta della tecnica di base per pf
                                    dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ListView lv = (dialog).getListView();
                                            Integer selected = (Integer)lv.getTag();
                                            if(lv.getTag() == null)
                                                Toast.makeText(MainActivity.this, "Select one basic method", Toast.LENGTH_SHORT).show();
                                            else {
                                                if (selected == 0) {
                                                    google = true;
                                                    p1 = true;
                                                } else if (selected == 1) {
                                                    nat = true;
                                                    p2 = true;
                                                }
                                                if (final_method == 3) {
                                                    String name = "/rilevamenti_geocomp";
                                                    folder = new File(Environment.getExternalStorageDirectory(), name);
                                                    if (!folder.exists()) {
                                                        folder.mkdirs();
                                                    }
                                                    long time = System.currentTimeMillis();

                                                    String data = format.format(time);

                                                    file = new File(folder + "/" + data + ".csv");
                                                    try {
                                                        FileWriter writer = new FileWriter(file, true);
                                                        String entries[] = {"Type;", "Time;", "Latitude;", "Longitude;", "Altitude\n"};
                                                        for (String str : entries) {
                                                            writer.write(str);
                                                        }
                                                        writer.close();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                    clearMap();
                                                    if (google)
                                                        googleAct();
                                                    if (nat)
                                                        nativeAct();
                                                }
                                                dialog1.dismiss();
                                            }
                                        }
                                    });
                                    break;
                            }
                        }

                        dialog.dismiss();
                    }
                }
            }
        });
    }

    public void plotOnMap(Double lat, Double lon, Double alt,  int type) {
        switch(type){
                //google
            case 1:
                now = new GeoPoint(lat, lon, alt);
                mapController.setCenter(now);
                Marker startMarker = new Marker(map);
                startMarker.setPosition(now);
                startMarker.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.google, null));
                map.getOverlays().add(startMarker);
                lstartMarker.add(startMarker);
                break;
                //native
            case 2:
                nowN = new GeoPoint(lat, lon, alt);
                mapController.setCenter(nowN);
                Marker startMarkerN = new Marker(map);
                startMarkerN.setPosition(nowN);
                startMarkerN.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.inside, null));
                map.getOverlays().add(startMarkerN);
                lstartMarkerN.add(startMarkerN);
                break;
                //particle filtering recv
            case 3:
                nowPf = new GeoPoint(lat, lon, alt);
                mapController.setCenter(nowPf);
                Marker startMarkerPf = new Marker(map);
                startMarkerPf.setPosition(nowPf);
                startMarkerPf.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.pf, null));
                map.getOverlays().add(startMarkerPf);
                lstartMarkerPf.add(startMarkerPf);
                break;
        }

    }

}




