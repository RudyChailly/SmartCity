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

import com.bumptech.glide.Glide;
import com.example.smartcity.R;
import com.example.smartcity.models.commerce.Commerce;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
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
            viewHolder.intitule = convertView.findViewById(R.id.offre_intitule);
            viewHolder.description = convertView.findViewById(R.id.offre_description);
            viewHolder.commerce =  convertView.findViewById(R.id.offre_commerce);
            viewHolder.prix =  convertView.findViewById(R.id.offre_prix);
            viewHolder.image =  convertView.findViewById(R.id.offre_image);
        }
        Offre offre = getItem(position);
        viewHolder.intitule.setText(offre.getIntituleCourt());
        viewHolder.description.setText(offre.getDescription());
        if (offre.getCommerce() != null) {
            viewHolder.commerce.setText(offre.getCommerce().getNom());
        }
        if (offre.getImageURL() != null) {
            Glide.with(getContext()).load(offre.getImageURL()).into(viewHolder.image);
        }
        DecimalFormat df = new DecimalFormat("0.00");
        viewHolder.prix.setText(df.format(offre.getPrix()) +" €");

        // TODO: viewHolder.image

        return convertView;
    }

    public boolean contains(Offre offre) {
        for (int i = 0; i < this.getCount(); i++) {
            if (this.getItem(i).equals(offre)) {
                return true;
            }
        }
        return false;
    }

    public void removeOffre(Offre offre) {
        for (int i = 0; i < this.getCount(); i++) {
            if (this.getItem(i).equals(offre)) {
                this.remove(this.getItem(i));
            }
        }
    }

    private class OffreViewHolder {
        public TextView intitule;
        public TextView description;
        public TextView commerce;
        public TextView prix;
        public ImageView image;
    }

}
