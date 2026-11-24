package com.example.karl_helloandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.karl_helloandroid.reseau.ClientApi;
import com.example.karl_helloandroid.reseau.Logement;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    public static final String CLE_ID = "id";

    private TextView txtDetails;
    private TextView etat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txtDetails = findViewById(R.id.txtDetails);
        etat = findViewById(R.id.etat);

        int id = getIntent().getIntExtra(CLE_ID, -1);

        if (id == -1) {
            afficherErreur("Identifiant du logement manquant.");
            return;
        }

        ClientApi.get()
                .trouverLogement(id)
                .enqueue(new Callback<Logement>() {

                    @Override
                    public void onResponse(
                            Call<Logement> call,
                            Response<Logement> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            Logement l = response.body();

                            etat.setVisibility(View.GONE);
                            txtDetails.setVisibility(View.VISIBLE);

                            String prix = String.format(
                                    Locale.CANADA_FRENCH,
                                    "%.2f $ / nuit",
                                    l.prixParNuit
                            );

                            txtDetails.setText(
                                    l.titre + "\n\n" +
                                            "Ville : " + l.ville + "\n" +
                                            "Prix : " + prix + "\n" +
                                            "Nombre de voyageurs : " +
                                            l.nbVoyageurs + "\n" +
                                            "Propriétaire : " +
                                            l.proprietaireNom
                            );

                        } else if (response.code() == 404) {

                            afficherErreur(
                                    "Ce logement n'existe plus (404)."
                            );

                        } else {

                            afficherErreur(
                                    "Erreur HTTP " + response.code()
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Logement> call,
                            Throwable t) {

                        afficherErreur(
                                "Serveur injoignable : " + t.getMessage()
                        );
                    }
                });
    }

    private void afficherErreur(String message) {

        txtDetails.setVisibility(View.GONE);
        etat.setText(message);
        etat.setVisibility(View.VISIBLE);
    }
}