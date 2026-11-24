package com.example.karl_helloandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.karl_helloandroid.reseau.ClientApi;
import com.example.karl_helloandroid.reseau.Logement;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListeActivity extends AppCompatActivity {

    private LinearLayout conteneurLogements;
    private TextView messageErreur;
    private EditText champVille;
    private Button boutonChercher;
    private Button btnConnexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);

        conteneurLogements = findViewById(R.id.conteneurLogements);
        messageErreur = findViewById(R.id.messageErreur);
        champVille = findViewById(R.id.champVille);
        boutonChercher = findViewById(R.id.boutonChercher);
        btnConnexion = findViewById(R.id.btnConnexionListe);

        boutonChercher.setOnClickListener(v -> {
            String ville = champVille.getText().toString().trim();
            chargerLogements(ville);
        });

        btnConnexion.setOnClickListener(v -> {
            Intent intent = new Intent(
                    ListeActivity.this,
                    ConnexionActivity.class
            );
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        chargerLogements(champVille.getText().toString().trim());
    }

    private void chargerLogements(String ville) {

        Call<List<Logement>> appel;

        if (ville.isEmpty()) {
            appel = ClientApi.get().listerLogements();
        } else {
            appel = ClientApi.get().chercherParVille(ville);
        }

        Log.d("LocaMobile", "1");

        appel.enqueue(new Callback<List<Logement>>() {

            @Override
            public void onResponse(
                    Call<List<Logement>> call,
                    Response<List<Logement>> response) {

                Log.d("LocaMobile", "3");
                Log.d("LocaMobile", "code " + response.code());

                if (response.isSuccessful()
                        && response.body() != null) {

                    List<Logement> logements = response.body();

                    Log.d("LocaMobile",
                            "Nombre de logements : " + logements.size());

                    if (logements.isEmpty() && !ville.isEmpty()) {
                        afficherErreur("Aucun logement à " + ville + ".");
                    } else {
                        afficher(logements);
                    }

                } else {
                    afficherErreur("Erreur HTTP " + response.code());
                }
            }

            @Override
            public void onFailure(
                    Call<List<Logement>> call,
                    Throwable t) {

                Log.e("LocaMobile",
                        "onFailure : " + t.getMessage(), t);

                afficherErreur(
                        "Serveur injoignable : " + t.getMessage()
                );
            }
        });

        Log.d("LocaMobile", "2");
    }

    private void afficher(List<Logement> logements) {

        conteneurLogements.removeAllViews();
        messageErreur.setVisibility(View.GONE);

        for (Logement l : logements) {

            View carte = getLayoutInflater().inflate(
                    R.layout.item_logement,
                    conteneurLogements,
                    false
            );

            TextView carteTitre = carte.findViewById(R.id.carteTitre);
            TextView carteVille = carte.findViewById(R.id.carteVille);
            TextView cartePrix = carte.findViewById(R.id.cartePrix);

            carteTitre.setText(l.titre);
            carteVille.setText(l.ville);

            cartePrix.setText(
                    String.format(
                            Locale.CANADA_FRENCH,
                            "%.2f $ / nuit",
                            l.prixParNuit
                    )
            );

            carte.setOnClickListener(v -> {

                Intent intent = new Intent(
                        ListeActivity.this,
                        DetailActivity.class
                );

                intent.putExtra("id", l.id);
                startActivity(intent);
            });

            conteneurLogements.addView(carte);
        }
    }

    private void afficherErreur(String message) {

        conteneurLogements.removeAllViews();
        messageErreur.setText(message);
        messageErreur.setVisibility(View.VISIBLE);
    }
}