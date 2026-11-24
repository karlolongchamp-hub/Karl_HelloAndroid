package com.example.karl_helloandroid;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.karl_helloandroid.reseau.ClientApi;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConnexionActivity extends AppCompatActivity {

    private EditText editUtilisateur;
    private EditText editMotDePasse;
    private TextView txtResultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        editUtilisateur = findViewById(R.id.editUtilisateur);
        editMotDePasse = findViewById(R.id.editMotDePasse);
        txtResultat = findViewById(R.id.txtResultat);

        Button btnConnexion = findViewById(R.id.btnConnexion);

        btnConnexion.setOnClickListener(v -> connecter());
    }

    private void connecter() {

        String courriel = editUtilisateur.getText().toString().trim();
        String motDePasse = editMotDePasse.getText().toString();

        if (courriel.isEmpty()) {
            editUtilisateur.setError("Entrez votre courriel");
            return;
        }

        if (motDePasse.isEmpty()) {
            editMotDePasse.setError("Entrez votre mot de passe");
            return;
        }

        txtResultat.setText("Connexion en cours...");

        ConnexionRequete requete =
                new ConnexionRequete(courriel, motDePasse);

        ClientApi.get()
                .connecter(requete)
                .enqueue(new Callback<TokenReponse>() {

                    @Override
                    public void onResponse(
                            Call<TokenReponse> call,
                            Response<TokenReponse> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            TokenReponse tokenReponse = response.body();

                            Session.enregistrer(
                                    ConnexionActivity.this,
                                    tokenReponse.token,
                                    tokenReponse.nom
                            );

                            chargerProfil(tokenReponse.token);

                        } else {

                            String message = "";

                            try {
                                if (response.errorBody() != null) {

                                    String texte =
                                            response.errorBody().string();

                                    JsonObject json = new Gson()
                                            .fromJson(texte, JsonObject.class);

                                    if (json != null
                                            && json.has("message")
                                            && !json.get("message").isJsonNull()) {

                                        message = json.get("message")
                                                .getAsString();
                                    }
                                }

                            } catch (Exception e) {
                                message = e.getMessage();
                            }

                            txtResultat.setText(
                                    "Erreur " + response.code()
                                            + " : " + message
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<TokenReponse> call,
                            Throwable t) {

                        txtResultat.setText(
                                "Serveur injoignable : " + t.getMessage()
                        );
                    }
                });
    }

    private void chargerProfil(String jeton) {

        ClientApi.get()
                .monProfil("Bearer " + jeton)
                .enqueue(new Callback<Profil>() {

                    @Override
                    public void onResponse(
                            Call<Profil> call,
                            Response<Profil> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            Profil profil = response.body();

                            txtResultat.setText(
                                    "HTTP " + response.code() + "\n\n"
                                            + "Nom : " + profil.nom + "\n"
                                            + "Rôle : " + profil.role + "\n"
                                            + "Courriel : " + profil.courriel
                            );

                        } else {

                            txtResultat.setText(
                                    "Erreur HTTP " + response.code()
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Profil> call,
                            Throwable t) {

                        txtResultat.setText(
                                "Serveur injoignable : " + t.getMessage()
                        );
                    }
                });
    }
}