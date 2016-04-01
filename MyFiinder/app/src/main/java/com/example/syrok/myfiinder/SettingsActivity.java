package com.example.syrok.myfiinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SettingsActivity extends Activity {
    private ListView mListInteret = null;
    private String[] mInteret = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mListInteret = (ListView) findViewById(R.id.listInteret);
        mInteret = new String[]{"Cinéma", "Musique", "Restaurant", "Sport"};
        mListInteret.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, mInteret));
        mListInteret.setItemChecked(1, true);


        final Button BSettValider = (Button) findViewById(R.id.BSettValider);
        BSettValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);

                startActivity(intent);
                //On déclare qu'on ne peut plus sélectionner d'élément
                mListInteret.setChoiceMode(ListView.CHOICE_MODE_NONE);
                //On affiche un layout qui ne permet pas de sélection
                mListInteret.setAdapter(new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_list_item_1, mInteret));


            }
        });
    }
}