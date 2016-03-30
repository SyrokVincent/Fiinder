package com.example.syrok.myfiinder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private ListView mListInteret = null;
    private Button mSend = null;
    private String[] mInteret = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListInteret = (ListView) findViewById(R.id.listInteret);
        mSend = (Button) findViewById(R.id.send);

        mInteret = new String[]{"Cinema", "Manger", "Restaurant", "Sport"};
        mListInteret.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, mInteret));
        mListInteret.setItemChecked(1, true);

        mSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Merci ! Les données ont été envoyées !", Toast.LENGTH_LONG).show();



                //On déclare qu'on ne peut plus sélectionner d'élément
                mListInteret.setChoiceMode(ListView.CHOICE_MODE_NONE);
                //On affiche un layout qui ne permet pas de sélection
                mListInteret.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, mInteret));

                //On désactive le bouton
                mSend.setEnabled(false);
            }
        });
    }
}