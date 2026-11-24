
package com.example.karl_helloandroid;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.List;

public class LogementAdapter extends RecyclerView.Adapter<LogementAdapter.ViewHolder> {

    private final List<JsonObject> logements;

    public LogementAdapter(List<JsonObject> logements) {
        this.logements = logements;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        textView.setTextSize(18);
        textView.setPadding(32, 24, 32, 24);

        return new ViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        JsonObject logement = logements.get(position);
        int id = logement.get("id").getAsInt();
        String titre = logement.has("titre") && !logement.get("titre").isJsonNull()
                ? logement.get("titre").getAsString() : "Sans titre";

        String ville = logement.has("ville") && !logement.get("ville").isJsonNull()
                ? logement.get("ville").getAsString() : "Ville inconnue";

        String prix = logement.has("prixParNuit") && !logement.get("prixParNuit").isJsonNull()
                ? logement.get("prixParNuit").getAsString() : "Prix non disponible";

        holder.textView.setText(
                titre + "\n" + ville + "\n" + prix + " $ / nuit"
        );

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("titre", titre);
            intent.putExtra("ville", ville);
            intent.putExtra("prix", prix);

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return logements.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}
