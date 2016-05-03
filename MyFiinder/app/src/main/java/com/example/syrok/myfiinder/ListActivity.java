package com.example.syrok.myfiinder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.FloatMath;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Activité permettant d'afficher les points d'intérêt proches de l'utilisateur sous forme de liste
 */
public class ListActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private ImageButton BMainImButton;
    private TextView pseudo = null;
    private String string_poi=""; // qui sera insérée dans la requete
    private int rayon;//qui sera inséré dans la requête
    private double userlatitude;
    private double userlongitude;

    private ArrayList<Lieu> liste_resultats;
    private GoogleApiClient mGoogleApiClient;
    ArrayList<GooglePlace> venuesList;
    final String GOOGLE_KEY = "AIzaSyAvLYD_2uSEhQd408pv_SElbr-ON5BcmIA"; //clef API devrait marcher sur android
    //final String GOOGLE_KEY = "AIzaSyD1J2tNd_u8BIpSa2DcA19V_MrH1wjCUCs";//clef serveur (pour windows)
    ArrayAdapter myAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pseudo = (TextView) findViewById(R.id.pseudo);
        /**
         * Récupère le pseudo de l'utilisateur grâce aux données passées par HomeActivity
         * Récupère aussi des premières coordonnées par HomeActivity (pour ne pas attendre une connexion au Service)
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
         * Récupère la position de l'utilisateur en temps réel avec Location Service
         */
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

        /**GO TO SETTINGS ACTIVITY
         * Met l'image dans le bouton et affecte un listener onclick
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
         * On récupère les préférences et on crée string_poi qui sera inclus dans la requête
         */
        generer_string_poi();
        //Toast toast = Toast.makeText(this, string_poi, Toast.LENGTH_LONG);
        //toast.show();

        /**
         * Récupère la liste grâce à getListData qui est une liste fixe pour le moment
         * à implémenter -> Récupération des points proches de nous en fonction de NOS coordonnées
         */
        //ArrayList lieu_details = getListData();

        buildGoogleApiClient();
        // start the AsyncTask that makes the call for the venus search.
        Googleplaces GP = new Googleplaces();
        GP.execute();
    }


    @Override
    public void onResume(){
        super.onResume();
        generer_string_poi();
        buildGoogleApiClient();
        // start the AsyncTask that makes the call for the venus search.
        Googleplaces GP = new Googleplaces();
        GP.execute();
        //Toast toast = Toast.makeText(this, string_poi, Toast.LENGTH_LONG);
        //toast.show();
    }
    private void generer_string_poi() {
        string_poi = "";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("poi_art", true)) string_poi += "museum|art_gallery";
        if (prefs.getBoolean("poi_culture", true) && !string_poi.isEmpty())  string_poi += "|library|university|book_store|school";
        if (prefs.getBoolean("poi_culture", true) && string_poi.isEmpty()) string_poi += "library|university|book_store|school";
        if (prefs.getBoolean("poi_argent", true) && !string_poi.isEmpty()) string_poi += "|bank|atm";
        if (prefs.getBoolean("poi_argent", true) && string_poi.isEmpty()) string_poi += "bank|atm";
        if (prefs.getBoolean("poi_nourriture_boissons", true) && !string_poi.isEmpty()) string_poi += "|restaurant|bakery|bar|cafe|food";
        if (prefs.getBoolean("poi_nourriture_boissons", true) && string_poi.isEmpty()) string_poi += "restaurant|bakery|bar|cafe|food";
        if (prefs.getBoolean("poi_divertissement", true) && !string_poi.isEmpty()) string_poi += "|amusement_park|casino|aquarium|movie_theater|zoo|bowling_alley|night_club";
        if (prefs.getBoolean("poi_divertissement", true) && string_poi.isEmpty()) string_poi += "amusement_park|casino|aquarium|movie_theater|zoo|bowling_alley|night_club";
        if (prefs.getBoolean("poi_shopping", true) && !string_poi.isEmpty()) string_poi += "|store|shoe_store|electronics_store|convenience_store|grocery_or_supermarket|home_goods_store|clothing_store";
        if (prefs.getBoolean("poi_shopping", true) && string_poi.isEmpty()) string_poi += "store|shoe_store|electronics_store|convenience_store|grocery_or_supermarket|home_goods_store|clothing_store";
        if (prefs.getBoolean("poi_sport", true) && !string_poi.isEmpty()) string_poi += "|gym|stadium";
        if (prefs.getBoolean("poi_sport", true) && string_poi.isEmpty())  string_poi += "gym|stadium";
        if (prefs.getBoolean("poi_sante", true) && !string_poi.isEmpty()) string_poi += "|health|dentist|doctor|hospital|pharmacy|veterinary_care";
        if (prefs.getBoolean("poi_sante", true) && string_poi.isEmpty()) string_poi += "health|dentist|doctor|hospital|pharmacy|veterinary_care";
        if (prefs.getBoolean("poi_bien_etre", true) && !string_poi.isEmpty()) string_poi += "|spa|hair_care|beauty_salon";
        if (prefs.getBoolean("poi_bien_etre", true) && string_poi.isEmpty()) string_poi += "spa|hair_care|beauty_salon";
        if (prefs.getBoolean("poi_religion", true) && !string_poi.isEmpty()) string_poi += "|cemetery|church|funeral_home|hindu_temple|synagogue";
        if (prefs.getBoolean("poi_religion", true) && string_poi.isEmpty()) string_poi += "cemetery|church|funeral_home|hindu_temple|synagogue";
    }

// Fonctions pour faire la requête

    private class Googleplaces extends AsyncTask<View, Integer, String> {

        String temp;

        @Override
        protected String doInBackground(View... urls) {
            String type ="";
            if(!string_poi.isEmpty())   type="&types=";
            // make Call to the url
            try {
                temp = makeCall("https://maps.googleapis.com/maps/api/place/search/json?location=" + userlatitude + "," + userlongitude + "&radius=1000&sensor=true&"+"key=" + GOOGLE_KEY+type+ URLEncoder.encode(string_poi, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //print the call in the console
            System.out.println("https://maps.googleapis.com/maps/api/place/search/json?location=" + userlatitude + "," + userlongitude + "&radius=1000hhhhhhhhhh&sensor=true&key=" + GOOGLE_KEY+type+string_poi);
            return "";
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }

        // Méthode exécutée à la fin de l'execution de la tâche asynchrone
        @Override
        protected void onPostExecute(String result) {

            if (temp == null) {
                // we have an error to the call
                // we can also stop the progress bar
            } else {
                // all things went right

                // parse Google places search result
                venuesList = parseGoogleParse(temp);
                liste_resultats = new ArrayList<>();
                for (int i = 0; i < venuesList.size(); i++) {
                    //Parsing de la note en float
                    float note = 0;
                    try{
                        note = Float.parseFloat(venuesList.get(i).getRating());
                    }catch(Exception e){System.out.println("ouch le parsing!");}
                    //Calcul de la distance
                    int dist = calcul_distance(venuesList.get(i).getLatitude(), venuesList.get(i).getLongitude(), userlatitude, userlongitude);
                    System.out.println(venuesList.get(i).getCategory());
                    Lieu l = new Lieu(venuesList.get(i).getName(), dist, venuesList.get(i).getCategory(), note, venuesList.get(i).getLatitude(), venuesList.get(i).getLongitude());
                    liste_resultats.add(l);
                }
                Collections.sort(liste_resultats);
                final ListView lv1 = (ListView) findViewById(R.id.location_list);
                lv1.setAdapter(new CustomListAdapter(ListActivity.this, liste_resultats));
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
                        intent.putExtra("nomlieu", l.getNom());
                        startActivity(intent);
                    }
                });
               // myAdapter = new ArrayAdapter(ApiGoogleBidouille.this, R.layout.row_layout, R.id.listText, listTitle);
               // Toast.makeText(getApplicationContext(), "Le traitement asynchrone est terminé", Toast.LENGTH_LONG).show();
            }

        }
    }

    public static String makeCall(String url) {

        // string buffers the url
        StringBuffer buffer_string = new StringBuffer(url);
        String replyString = "";

        // instanciate an HttpClient
        HttpClient httpclient = new DefaultHttpClient();
        // instanciate an HttpGet
        HttpGet httpget = new HttpGet(buffer_string.toString());

        try {
            // get the responce of the httpclient execution of the url
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();

            // buffer input stream the result
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(20);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            // the result as a string is ready for parsing
            replyString = new String(baf.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(replyString);

        // trim the whitespaces
        return replyString.trim();
    }

    private static ArrayList parseGoogleParse(final String response) {

        ArrayList temp = new ArrayList();
        try {

            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);

            // make an jsonObject in order to parse the response
            if (jsonObject.has("results")) {

                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    GooglePlace poi = new GooglePlace();
                    if (jsonArray.getJSONObject(i).has("name")) {
                        poi.setName(jsonArray.getJSONObject(i).optString("name"));
                        poi.setRating(jsonArray.getJSONObject(i).optString("rating", " "));

                        poi.setLatitude(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").optDouble("lat"));
                        poi.setLongitude(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").optDouble("lng"));
                        if (jsonArray.getJSONObject(i).has("opening_hours")) {
                            if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
                                    poi.setOpenNow("YES");
                                } else {
                                    poi.setOpenNow("NO");
                                }
                            }
                        } else {
                            poi.setOpenNow("Not Known");
                        }
                        if (jsonArray.getJSONObject(i).has("types")) {
                            JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");

                            for (int j = 0; j < typesArray.length(); j++) {
                                poi.setCategory(typesArray.getString(j) + ", " + poi.getCategory());
                            }
                        }
                        if(jsonArray.getJSONObject(i).has("location")){

                        }
                    }
                    temp.add(poi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
        System.out.println("-------------- Nombre de réultats de la requête : "+temp.size()+" ------------------");
        return temp;

    }



    /**
     * Builds a GoogleApiClient. Uses {@code #addApi} to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener( this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    public double en_radian(double a){
        return (Math.PI*a)/180;
    }
    private int calcul_distance(double lat_a, double lng_a, double lat_b, double lng_b) {
        float pk = (float) (180/3.14169);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
        double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
        double t3 = Math.sin(a1)* Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);
        System.out.println(6366000*tt);

        return (int)(6366000*tt);
    }
    /*public int calcul_distance(double lieulat, double lieulng, double userlat, double userlng){
        int R = 6378000; //Rayon de la terre en mètre
        lieulat = en_radian(lieulat);
        lieulng = en_radian(lieulng);
        userlat = en_radian(userlat);
        userlng = en_radian(userlng);
        double res = (R *(Math.PI/2 - Math.asin( Math.sin(userlat) * Math.sin(lieulat) + Math.cos(userlng - lieulng) * Math.cos(userlng) * Math.cos(lieulat))));
        System.out.println(res);
        return (int)res;

    }*/

}