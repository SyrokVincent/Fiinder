package com.example.syrok.myfiinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Classe qui affichera la carte, centrée sur le point que nous avons demandé
 * En s'ouvrant elle récupère les informations que ListActivity lui a fournies (latitude et longitude)
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap = googleMap;
    /*
        Permet de récupérer les informations de l'intent, passées au travers de la classe ListActivity
        et d'afficher la carte avec les coordonnées récupérées
    */
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null) {
            double lieulongitude = extras.getDouble("lieulatitude");
            double lieulatitude = extras.getDouble("lieulongitude");
            double userlatitude = extras.getDouble("userlatitude");
            double userlongitude = extras.getDouble("userlongitude");
            LatLng lieu = new LatLng(userlatitude, userlongitude);
            GoogleMapOptions optionsmap = new GoogleMapOptions();
            CameraPosition position = new CameraPosition(lieu,14,0,0);

            optionsmap.camera(position);
            optionsmap.compassEnabled(true);

            mMap.addMarker(new MarkerOptions().position(lieu).title("Lieu choisi"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lieu,14));

        }
    }
}
