package com.example.googlemaps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    EditText et;
    Button bt;
    String title[]=new String[10];
    LatLng lng[]=new LatLng[10];
    CameraUpdate cu[]=new CameraUpdate[10];
    MarkerOptions mp[]=new MarkerOptions[10];
    LatLng l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        et = findViewById(R.id.search);
        bt = findViewById(R.id.btn_search);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Toast.makeText(MapsActivity.this, "working", Toast.LENGTH_SHORT).show();
                    Geocoder g = new Geocoder(MapsActivity.this);
                    List<Address> locations = g.getFromLocationName(et.getText().toString(), 10);
                    Address address = locations.get(0);
                    String title = address.getAddressLine(0) + "," + address.getAddressLine(1) + "," + address.getLocality() + "," + address.getSubLocality() + "," + address.getPostalCode() + "," + address.getCountryName();
                    LatLng lng = new LatLng(address.getLatitude(), address.getLongitude());
                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(lng, 20);
                    MarkerOptions mp = new MarkerOptions();
                    mp.title(title);
                    mp.position(lng);
                    mMap.addMarker(mp);
                    mMap.animateCamera(cu);
                    Toast.makeText(MapsActivity.this, title, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    System.out.println("error :" + e.getMessage());
                    Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "Wait", Toast.LENGTH_SHORT).show();
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 100, this);
    }
    @Override
    public void onLocationChanged(Location location) {
        l=new LatLng(location.getLatitude(),location.getLongitude());
        Toast.makeText(this, "Your GPS is now started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi=getMenuInflater();
        mi.inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_satellite)
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        else if(item.getItemId()==R.id.menu_terrain)
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        else if(item.getItemId()==R.id.menu_hybrid)
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        else if(item.getItemId()==R.id.menu_locateme)
        {
            if(l==null)
                Toast.makeText(this, "Please wait....your GPS is not working", Toast.LENGTH_SHORT).show();
            else
            {
                CameraUpdate c=CameraUpdateFactory.newLatLngZoom(l,20);
                mMap.animateCamera(c);
                MarkerOptions mp=new MarkerOptions();
                mp.position(l);
                mp.title("your location");
                mMap.addMarker(mp);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }



    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
