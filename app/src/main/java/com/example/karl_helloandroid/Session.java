package com.example.karl_helloandroid;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private static final String FICHIER = "session";

    public static void enregistrer(Context context, String jeton, String nom) {
        SharedPreferences preferences =
                context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE);

        preferences.edit()
                .putString("jeton", jeton)
                .putString("nom", nom)
                .apply();
    }

    public static String jeton(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE);

        return preferences.getString("jeton", null);
    }

    public static String nom(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE);

        return preferences.getString("nom", "");
    }
}