package com.example.syrok.myfiinder;

import android.app.ListActivity;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
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
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final Button b = (Button) findViewById(R.id.bouton1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
                //On déclare qu'on ne peut plus sélectionner d'élément
                mListInteret.setChoiceMode(ListView.CHOICE_MODE_NONE);
                //On affiche un layout qui ne permet pas de sélection
                mListInteret.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, mInteret));


            }
        });
    }

}