package com.example.syrok.myfiinder;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;

public class MapActivity extends FragmentActivity {

    private GoogleMap map;

    private double userlatitude;
    private double userlongitude;
    private double placelatitude;
    private double placelongitude;
    private String placename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //On récupère les infos de là où on a cliqué
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null) {
            placelongitude = extras.getDouble("lieulatitude");
            placelatitude = extras.getDouble("lieulongitude");
            userlatitude = extras.getDouble("userlatitude");
            userlongitude = extras.getDouble("userlongitude");
            placename = extras.getString("nomlieu");
        }
        LatLng userposition = new LatLng(userlatitude,userlongitude);
        LatLng placeposition = new LatLng(placelatitude,placelongitude);

        //on récupère le fragment définit dans le layout qui va contenir la carte
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        //on s'assure que l'objet map n'est pas vide
        if (map != null) {
            //on autorise l'api à afficher le bouton pour accéder à notre position courante
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Vide mais random
            }
            map.setMyLocationEnabled(true);
            MarkerOptions markeruser = new MarkerOptions();
            markeruser.title("YOU");
            markeruser.position(userposition);
            markeruser.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            map.addMarker(markeruser);

            MarkerOptions markerplace = new MarkerOptions();
            markerplace.title(placename);
            markerplace.position(placeposition);
            markerplace.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            map.addMarker(markerplace);
            // On affiche la carte sur le milieu des deux positions
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(userposition, 15));
            map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
        }
    }

}