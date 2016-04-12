package com.example.syrok.myfiinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Activité principale, celle où on arrive en démarrant l'application
 * On peut s'y incrire, se connecter ou cliquer sur "continuer en tant qu'invité"
 */
public class HomeActivity extends Activity  {


    protected Button BHomeConnexion;

    protected TextView mAddressText;
    private boolean inscriptionok = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        /**
         * On démarre le service de localisation et on affiche les coordonnées
         */
        Intent intent = new Intent(HomeActivity.this, LocationService.class);
        startService(intent);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        double latitude = intent.getDoubleExtra(LocationService.EXTRA_LATITUDE, 0);
                        double longitude = intent.getDoubleExtra(LocationService.EXTRA_LONGITUDE, 0);
                        String adresse = intent.getStringExtra(LocationService.EXTRA_ADDRESS);
                        //Localisation en cours :
                        if(adresse==null){
                            Toast toast = Toast.makeText(context, "Localisation en cours", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        if(adresse!=null){
                            Toast toast = Toast.makeText(context, "Localisation trouvée : \n"+latitude+" "+longitude+"\n"+adresse, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                }, new IntentFilter(LocationService.ACTION_LOCATION_BROADCAST)
        );

        /**
         * Bouton qui permet d'accéder à la liste des points d'intérêt sans se connecter
         * */
        final Button BhomeSansenreg = (Button) findViewById(R.id.BhomeSansenreg);
        BhomeSansenreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ListActivity.class);
                intent.putExtra("pseudo", "Invité");
                startActivity(intent);
            }
        });

        /**
         * Bouton qui renvoie à l'activité Inscription
         */
        final Button BhomeInscription = (Button) findViewById(R.id.BHomeInscription);
        BhomeInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(HomeActivity.this, InscriptionActivity.class);
                startActivity(intent2);
            }
        });


        /**
         * bouton qui permet de se connecter
         */
        BHomeConnexion = (Button) findViewById(R.id.BHomeConnexion);
        BHomeConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(HomeActivity.this, ApiGoogleBidouille.class);
                startActivity(intent2);
            }
        });

        /**
         * Vérifie si on vient de s'inscrire. Dans ce cas là affiche une pop-up
         */
        Intent i = getIntent();
        Bundle e = i.getExtras();
        if (e != null) {
            inscriptionok = (e.getBoolean("inscriptionok"));
        }
        if(inscriptionok){
            new AlertDialog.Builder(this)
                    .setTitle("Inscription terminée")
                    .setMessage("Félicitations, vous êtes bien inscrit. Vous pouvez désormais vous connecter.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.
        startService(new Intent(this, LocationService.class));

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

   }

