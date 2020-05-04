package com.example.smartcity.models.actualite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcity.R;

import java.util.List;

public class ActualiteAdapter extends ArrayAdapter<Actualite> {

    public ActualiteAdapter(Context context, List<Actualite> actualites) {
        super(context, 0, actualites);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.actualite, parent, false);
        }
        ActualiteViewHolder viewHolder = (ActualiteViewHolder)convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ActualiteViewHolder();
            viewHolder.titre = (TextView)convertView.findViewById(R.id.actualite_titre);
            viewHolder.source = (TextView)convertView.findViewById(R.id.actualite_source);
            viewHolder.interet = (TextView)convertView.findViewById(R.id.actualite_interet);
        }
        Actualite actualite = getItem(position);
        viewHolder.titre.setText(actualite.getTitre());
        viewHolder.source.setText(actualite.getSource());
        if (actualite.getInteret() != null) {
            viewHolder.interet.setText(actualite.getInteret().toString());
        }
        return convertView;
    }

    private class ActualiteViewHolder {
        public TextView titre;
        public TextView source;
        public TextView interet;
    }
}
