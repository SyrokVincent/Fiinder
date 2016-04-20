package com.example.syrok.myfiinder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Activité permettant d'afficher les points d'intérêt proches de l'utilisateur sous forme de liste
 */
public class ListActivity extends Activity  {
    private ImageButton BMainImButton;
    private TextView pseudo = null;
    private String string_poi=""; // qui sera insérée dans la requete
    private double userlatitude;
    private double userlongitude;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(ListActivity.this, LocationService.class);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {

                        userlatitude = intent.getDoubleExtra(LocationService.EXTRA_LATITUDE, 0);
                        userlongitude = intent.getDoubleExtra(LocationService.EXTRA_LONGITUDE, 0);

                            //Toast toast = Toast.makeText(context, "Localisation trouvée : \n"+userlatitude+" "+userlongitude+"\n", Toast.LENGTH_LONG);
                            //toast.show();

                    }
                }, new IntentFilter(LocationService.ACTION_LOCATION_BROADCAST)
        );


        pseudo = (TextView) findViewById(R.id.pseudo);
        /**
         * Récupère le pseudo de l'utilisateur grâce aux données passées par HomeActivity
         */
        Intent i = getIntent();
        Bundle e = i.getExtras();
        pseudo.setText("invité");
        if (e != null) {
            pseudo.setText(e.getString("pseudo"));
            userlatitude = e.getDouble("firstlatitude");
            userlongitude = e.getDouble("firstlongitude");
        }

        /**
         * Met l'image dans le bouton et affecte un listener onclick
         * Si aucune préférence n'est rentrée, lui envoie une liste avec que le premier élément de coché
         * Sinon renvoie à la page des settings les informations que le client a rentrées
         */

        BMainImButton = (ImageButton) findViewById(R.id.imageButton);
        BMainImButton.setImageResource(R.drawable.settings);
        BMainImButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ListActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        /**
         * On récupère les préférences et on crée string_poi
         */
        generer_string_poi();
        Toast toast = Toast.makeText(this, string_poi, Toast.LENGTH_LONG);
        toast.show();
        /**
         * Récupère la liste grâce à getListData qui est une liste fixe pour le moment
         * à implémenter -> Récupération des points proches de nous en fonction de NOS coordonnées
         */
        ArrayList lieu_details = getListData();

        final ListView lv1 = (ListView) findViewById(R.id.location_list);
        lv1.setAdapter(new CustomListAdapter(this, lieu_details));
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Gère le clic sur un élément de la ListView, récupère sa position et envoie les données longitude et latitude
             * à la map qui sera ouverte ensuite (MapActivity)
             * longitude et latitude sont passés en extra dans l'activité MapActivity
             */
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Lieu l = (Lieu) lv1.getAdapter().getItem(position);
                Intent intent = new Intent(ListActivity.this, MapActivity.class);
                intent.putExtra("lieulongitude", l.getLongitude());
                intent.putExtra("lieulatitude", l.getLatitude());
                intent.putExtra("userlatitude", userlatitude);
                intent.putExtra("userlongitude", userlongitude);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onResume(){
        super.onResume();
        generer_string_poi();
        Toast toast = Toast.makeText(this, string_poi, Toast.LENGTH_LONG);
        toast.show();
    }
    private void generer_string_poi(){
        string_poi="";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean("poi_art",true))    string_poi+="museum|art_gallery";
        if(prefs.getBoolean("poi_culture",true) && !string_poi.isEmpty()) string_poi+="|library|university|book_store|school";
        if(prefs.getBoolean("poi_culture",true) && string_poi.isEmpty()) string_poi+="library|university|book_store|school";
        if(prefs.getBoolean("poi_argent",true) && !string_poi.isEmpty()) string_poi+="|bank|atm";
        if(prefs.getBoolean("poi_argent",true) && string_poi.isEmpty()) string_poi+="bank|atm";
        if(prefs.getBoolean("poi_nourriture_boissons",true) && !string_poi.isEmpty()) string_poi+="|restaurant|bakery|bar|cafe|food";
        if(prefs.getBoolean("poi_nourriture_boissons",true) && string_poi.isEmpty()) string_poi+="restaurant|bakery|bar|cafe";
        if(prefs.getBoolean("poi_divertissement",true) && !string_poi.isEmpty()) string_poi+="|amusement_park|casino|aquarium|movie_theater|zoo|bowling_alley|night_club";
        if(prefs.getBoolean("poi_divertissement",true) && string_poi.isEmpty()) string_poi+="amusement_park|casino|aquarium|movie_theater|zoo|bowling_alley|night_club";
        if(prefs.getBoolean("poi_shopping",true) && !string_poi.isEmpty()) string_poi+="|store|shoe_store|electronics_store|convenience_store|grocery_or_supermarket|home_goods_store|clothing_store";
        if(prefs.getBoolean("poi_shopping",true) && string_poi.isEmpty()) string_poi+="store|shoe_store|electronics_store|convenience_store|grocery_or_supermarket|home_goods_store|clothing_store";
        if(prefs.getBoolean("poi_sport",true) && !string_poi.isEmpty()) string_poi+="|gym|stadium";
        if(prefs.getBoolean("poi_sport",true) && string_poi.isEmpty()) string_poi+="gym|stadium";
        if(prefs.getBoolean("poi_sante",true) && !string_poi.isEmpty()) string_poi+="|health|dentist|doctor|hospital|pharmacy|veterinary_care";
        if(prefs.getBoolean("poi_sante",true) && string_poi.isEmpty()) string_poi+="health|dentist|doctor|hospital|pharmacy|veterinary_care";
        if(prefs.getBoolean("poi_bien_etre",true) && !string_poi.isEmpty()) string_poi+="|spa|hair_care|beauty_salon";
        if(prefs.getBoolean("poi_bien_etre",true) && string_poi.isEmpty()) string_poi+="spa|hair_care|beauty_salon";
        if(prefs.getBoolean("poi_religion",true) && !string_poi.isEmpty()) string_poi+="|cemetery|church|funeral_home|hindu_temple|synagogue";
        if(prefs.getBoolean("poi_religion",true) && string_poi.isEmpty()) string_poi+="cemetery|church|funeral_home|hindu_temple|synagogue";

    }
    /**
     * Permet de renvoyer la liste des points d'intérêt à proximité
     * à implémenter avec l'API Google Maps
     * @return
     */
    private ArrayList getListData() {
        ArrayList<Lieu> results = new ArrayList<Lieu>();
        Lieu l1 = new Lieu("Luminien", 200, "Restaurant", 4, 150, -13);
        Lieu l2 = new Lieu("BU Luminy", 150, "Bibliothèque", (float) 3.5, 40, 35);
        Lieu l3 = new Lieu("PizzaRedon", 350, "Fast-food", 5, 8, 22);
        Lieu l4 = new Lieu("Laverie Luminy", 100, "Laverie", 2, 11, -9);
        results.add(l1);
        results.add(l2);
        results.add(l3);
        results.add(l4);
        /*
        Trie la liste en fonction de la distance
         */
        Collections.sort(results);
        return results;
    }


}