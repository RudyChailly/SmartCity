package com.example.smartcity.models.commerce;

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
import com.example.smartcity.models.actualite.Actualite;
import com.example.smartcity.models.actualite.ActualiteAdapter;
import com.example.smartcity.models.commerce.Commerce;

import java.util.List;

public class CommerceAdapter  extends ArrayAdapter<Commerce> {

    public CommerceAdapter(Context context, List<Commerce> commerces) {
        super(context, 0, commerces);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.commerce, parent, false);
        }
        CommerceViewHolder viewHolder = (CommerceViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new CommerceViewHolder();
            viewHolder.nom = (TextView)convertView.findViewById(R.id.commerce_nom);
            viewHolder.adresse = (TextView)convertView.findViewById(R.id.commerce_adresse);
            viewHolder.suivi = (ImageView) convertView.findViewById(R.id.commerce_suivi);
        }
        Commerce commerce = getItem(position);
        viewHolder.nom.setText(commerce.getNom());
        viewHolder.adresse.setText(commerce.getAdresse());

        // TODO: viewHolder.suivi

        return convertView;
    }

    private class CommerceViewHolder {
        public TextView nom;
        public TextView adresse;
        public ImageView suivi;
    }

}
