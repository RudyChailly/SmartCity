package com.example.smartcity.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.smartcity.Activities.MainActivity;
import com.example.smartcity.R;
import com.example.smartcity.Models.Commerce;

import java.util.List;

public class CommerceAdapter  extends ArrayAdapter<Commerce> {

    private Context context;

    public CommerceAdapter(Context context, List<Commerce> commerces) {
        super(context, 0, commerces);
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.commerce, parent, false);
        }
        CommerceViewHolder viewHolder = (CommerceViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new CommerceViewHolder();
            viewHolder.nom = convertView.findViewById(R.id.commerce_nom);
            viewHolder.interet = convertView.findViewById(R.id.commerce_interet);
            viewHolder.ville =  convertView.findViewById(R.id.commerce_ville);
            viewHolder.abonne = convertView.findViewById(R.id.commerce_abonne);
            viewHolder.image = convertView.findViewById(R.id.commerce_image);
        }
        final Commerce commerce = getItem(position);
        viewHolder.nom.setText(commerce.getNom());
        if (commerce.getInteret() != null) {
            viewHolder.interet.setText(commerce.getInteret().toString());
        }
        if (commerce.getImageURL() != null) {
            Glide.with(getContext()).load(commerce.getImageURL()).into(viewHolder.image);
        }
        if (commerce.getVille() != null) {
            viewHolder.ville.setText(commerce.getVille().toString());
        }
        if (commerce.estAbonne()) {
            viewHolder.abonne.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
        else {
            viewHolder.abonne.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        final CommerceViewHolder finalViewHolder = viewHolder;
        viewHolder.abonne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commerce.estAbonne()) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle(getContext().getResources().getString(R.string.se_desabonner)+" "+commerce.getNom()+" ?");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton(getContext().getResources().getString(R.string.oui), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            commerce.desabonner();
                            ((MainActivity)getContext()).requestCommerceDesabonner(commerce);
                            Toast.makeText(getContext(), commerce.getNom() + " " + getContext().getResources().getString(R.string.plus_suivi), Toast.LENGTH_SHORT).show();
                            finalViewHolder.abonne.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        }
                    });
                    dialog.setNegativeButton(getContext().getResources().getString(R.string.non), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = dialog.create();
                    alert.show();
                }
                else {
                    commerce.abonner();
                    ((MainActivity)getContext()).requestCommerceAbonner(commerce);
                    Toast.makeText(getContext(), commerce.getNom() + " " + getContext().getResources().getString(R.string.suivi), Toast.LENGTH_SHORT).show();
                    finalViewHolder.abonne.setImageResource(R.drawable.ic_favorite_black_24dp);
                }
            }
            });

        return convertView;
    }

    public boolean contains(Commerce commerce) {
        for (int i = 0; i < this.getCount(); i++) {
            if (this.getItem(i).getId() == commerce.getId()) {
                return true;
            }
        }
        return false;
    }

    private class CommerceViewHolder {
        public TextView nom;
        public ImageView image;
        public TextView interet;
        public TextView ville;
        public ImageView abonne;
    }

}
