package uk.ac.tees.aad.W9493488;

import  androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Dashboard  extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static LatLng latLng;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        TextView name = findViewById(R.id.nameText);
        SharedPreferences sharedPreferences = getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        name.setText(sharedPreferences.getString("name",""));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location = new LatLng(51.5074,0.1278);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f));
        getCurrentLatLng(googleMap);
    }

    private void getCurrentLatLng(final GoogleMap googleMap){
        ActivityCompat.requestPermissions( this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            new AlertDialog.Builder(this).setMessage("Turn on the GPS").create().show();
        }
        else {

            boolean grantedPermissions =(
                    ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            );

            if (grantedPermissions)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else {
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location)
                            {
                                mMap = googleMap;
                                LatLng loc = new LatLng(location.getLatitude(),location.getLongitude());
                                latLng = loc;
                                mMap.addMarker(new MarkerOptions().position(loc).title("MY Location"));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 2.0f));
                                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                                List<Address>  addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 2);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                                Toast.makeText(getApplicationContext(),address,Toast.LENGTH_LONG).show();
                                SharedPreferences sharedPreferences = getSharedPreferences("location",MODE_PRIVATE);
                                SharedPreferences.Editor editer = sharedPreferences.edit();
                                editer.putFloat("lat",(float)location.getLatitude());
                                editer.putFloat("lng",(float)location.getLongitude());
                                editer.apply();
                            }
                        });
            }

        }
    }

}
