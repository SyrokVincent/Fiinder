package com.example.syrok.myfiinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Classe qui affichera la carte, centrée sur le point que nous avons demandé
 * En s'ouvrant elle récupère les informations que MainActivity lui a fournies (latitude et longitude)
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
        Permet de récupérer les informations de l'intent, passées au travers de la classe MainActivity
        et d'afficher la carte avec les coordonnées récupérées
    */
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null) {
            int longitude = extras.getInt("latitude");
            int latitude = extras.getInt("longitude");

            LatLng lieu = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(lieu).title("Lieu choisi"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(lieu));
        }
    }
}
