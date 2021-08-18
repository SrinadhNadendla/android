package uk.ac.tees.aad.W9493488;

import  androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    String address;
    String LatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        TextView name = findViewById(R.id.nameText);
        TextView logout = findViewById(R.id.logoutText);
        Button btn = findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),previousBookings.class);
                SharedPreferences sharedPreferences = getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
                intent.putExtra("email",sharedPreferences.getString("email",""));
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("loginDetails", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("login","no");
                editor.apply();
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
        Button book = findViewById(R.id.bookawash);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =  new Intent(getApplicationContext(),booking.class);
                intent.putExtra("latlng",LatLng);
                intent.putExtra("address",address);
                startActivity(intent);
            }
        });
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
        updatedata(googleMap);
    }

    private void updatedata(final GoogleMap googleMap){
        ActivityCompat.requestPermissions( this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            new AlertDialog.Builder(this).setMessage("Turn on the GPS").create().show();
        }
        else {

            if(
                    ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            )
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
                                    addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 3);
                                    address = addresses.get(0).getAddressLine(0);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getApplicationContext(),address,Toast.LENGTH_LONG).show();
                                LatLng= loc.toString();
                            }
                        });
            }

        }
    }

}
