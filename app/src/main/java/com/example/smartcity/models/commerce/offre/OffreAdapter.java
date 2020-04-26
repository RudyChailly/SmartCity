package com.example.smartcity.models.commerce.offre;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smartcity.R;

import org.w3c.dom.Text;

import java.util.List;

public class OffreAdapter extends ArrayAdapter<Offre> {

    public OffreAdapter(Context context, List<Offre> offres) {
        super(context, 0, offres);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.offre, parent, false);
        }
        OffreViewHolder viewHolder = (OffreViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new OffreViewHolder();
            viewHolder.intitule = (TextView)convertView.findViewById(R.id.offre_intitule);
            viewHolder.description = (TextView)convertView.findViewById(R.id.offre_description);
            viewHolder.commerce = (TextView) convertView.findViewById(R.id.offre_commerce);
            viewHolder.prix = (TextView) convertView.findViewById(R.id.offre_prix);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.offre_image);
        }
        Offre offre = getItem(position);
        viewHolder.intitule.setText(offre.getIntituleCourt());
        viewHolder.description.setText(offre.getDescription());
        viewHolder.commerce.setText(offre.getCommerce().getNom());
        viewHolder.prix.setText(offre.getPrix()+" €");

        // TODO: viewHolder.image

        return convertView;
    }

    private class OffreViewHolder {
        public TextView intitule;
        public TextView description;
        public TextView commerce;
        public TextView prix;
        public ImageView image;
    }

}
