package com.example.smartcity.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.smartcity.R;
import com.example.smartcity.Models.Actualite;

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
            viewHolder.image = convertView.findViewById(R.id.actualite_image);
        }
        Actualite actualite = getItem(position);
        if (actualite.getImageURL() != null) {
            Glide.with(getContext()).load(actualite.getImageURL()).into(viewHolder.image);
        }
        viewHolder.titre.setText(actualite.getTitre());
        viewHolder.source.setText(actualite.getSource());
        if (actualite.getInteret() != null) {
            viewHolder.interet.setText(actualite.getInteret().toString());
        }
        return convertView;
    }

    public boolean contains(Actualite actualite) {
        for (int i = 0; i < this.getCount(); i++) {
            if (this.getItem(i).equals(actualite)) {
                return true;
            }
        }
        return false;
    }

    private class ActualiteViewHolder {
        public TextView titre;
        public TextView source;
        public TextView interet;
        public ImageView image;
    }
}
