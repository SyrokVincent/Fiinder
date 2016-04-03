package com.example.syrok.myfiinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Activité principale, celle où on arrive en démarrant l'application
 * On peut s'y incrire, se connecter ou cliquer sur "continuer en tant qu'invité"
 */
public class HomeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /**
         * Bouton qui permet d'accéder à la liste des points d'intérêt sans se connecter
        * */
        final Button BhomeSansenreg = (Button) findViewById(R.id.BhomeSansenreg);
        BhomeSansenreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.putExtra("pseudo", "Invité");
                startActivity(intent);
            }
        });


    }
}
