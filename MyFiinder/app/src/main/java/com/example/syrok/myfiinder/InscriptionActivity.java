package com.example.syrok.myfiinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Océane on 02/04/2016.
 */
public class InscriptionActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        /**
         * Bouton qui renvoie à l'activité Inscription
         */
        final Button BInscrValider = (Button) findViewById(R.id.BInscrValider);
        BInscrValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InscriptionActivity.this, HomeActivity.class);

                if(okenvoi()){
                    intent.putExtra("inscriptionok", true);
                    startActivity(intent);
                }

            }
        });
    }

    /**
     * Fonction de vérification d'envoi du formulaire
     * @return true si tous les champs sont correctement rentrés , false sinon
     */
    public boolean okenvoi(){
        EditText Nom = (EditText) findViewById(R.id.inputNom);
        EditText Prenom = (EditText) findViewById(R.id.inputPrenom);
        EditText Pseudo = (EditText) findViewById(R.id.inputPseudo);
        EditText Mail = (EditText) findViewById(R.id.inputMail);
        EditText Mdp = (EditText) findViewById(R.id.inputMdp);
        EditText Confirmer = (EditText) findViewById(R.id.inputConfirmer);

        //Vérifie si des champs sont manquants
        if(isEmpty(Nom) || isEmpty(Prenom) || isEmpty(Pseudo)|| isEmpty(Mail) || isEmpty(Mdp) || isEmpty(Confirmer)){
            new AlertDialog.Builder(this)
                    .setTitle("Champ Manquant")
                    .setMessage("Veuillez remplir tous les champs")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }

        //Vérifie si l'email est au bon format
        String Mailtxt = Mail.getText().toString();
        if(!isEmailValid(Mailtxt)){
            new AlertDialog.Builder(this)
                    .setTitle("Email invalide")
                    .setMessage("Veuillez saisir une adresse mail valide")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }

        //Vérifie si les deux MDP sont identiques
        String Mdptxt = Mdp.getText().toString();
        String Confirmertxt = Confirmer.getText().toString();
        if(!Mdptxt.equals(Confirmertxt)){
            new AlertDialog.Builder(this)
                    .setTitle("Mots de Passe différents")
                    .setMessage("Veuillez saisir deux mots de passe identiques.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }

        //Vérifie que le pseudo n'est pas déjà choisi
        String Pseudotxt = Pseudo.getText().toString();
            //à remplir : Fonction pour vérifier que le pseudo est dans la BDD ou pas
        if(false){
            new AlertDialog.Builder(this)
                    .setTitle("Pseudo déjà utilisé")
                    .setMessage("Veuillez choisir un autre pseudo.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }
        return true;
    }

    /**
     * Vérifie qu'un champ EditText est vide ou non
     * @param etText L'EditText à vérifier
     * @return true si le champ n'a pas été rempli, false sinon
     */
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    /**
     * Vérifie que l'adresse mail est au bon format
     * @param email
     * @return true si l'email est valide, false sinon
     */
    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches()) return true;
        else return false;
    }

}