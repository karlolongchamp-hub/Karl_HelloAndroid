package com.example.karl_helloandroid.reseau;

import com.example.karl_helloandroid.ConnexionRequete;
import com.example.karl_helloandroid.TokenReponse;
import com.example.karl_helloandroid.Profil;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Body;
import retrofit2.http.Header;

public interface LocaMobileApi {

    @GET("api/logements")
    Call<List<Logement>> listerLogements();

    @GET("api/logements/{id}")
    Call<Logement> trouverLogement(@Path("id") int id);

    @GET("api/logements")
    Call<List<Logement>> chercherParVille(
            @Query("ville") String ville
    );

    @POST("api/auth/connexion")
    Call<TokenReponse> connecter(
            @Body ConnexionRequete requete
    );

    @GET("api/moi")
    Call<Profil> monProfil(
            @Header("Authorization") String entete
    );
}