
package com.example.karl_helloandroid.api;

import com.example.karl_helloandroid.ConnexionRequete;
import com.example.karl_helloandroid.TokenReponse;
import com.example.karl_helloandroid.Profil;

import com.google.gson.JsonObject;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("api/logements")
    Call<List<JsonObject>> getLogements();

    @GET("api/logements/{id}")
    Call<JsonObject> getLogementById(
            @Path("id") int id
    );

    @GET("api/logements")
    Call<List<JsonObject>> chercherParVille(
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
