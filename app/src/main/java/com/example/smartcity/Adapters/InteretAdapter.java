package com.example.smartcity.Adapters;

import android.content.Context;
import android.graphics.Color;
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
import com.example.smartcity.Models.Interet;
import com.example.smartcity.Activities.ChoixInteretsActivity;

import java.util.List;

public class InteretAdapter extends ArrayAdapter<Interet> {

    public InteretAdapter(Context context, List<Interet> interets) {
        super(context, 0, interets);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.interet, parent, false);
        }
        InteretViewHolder viewHolder = (InteretViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new InteretViewHolder();
            viewHolder.nom = convertView.findViewById(R.id.interet_nom);
            viewHolder.image = convertView.findViewById(R.id.interet_image);
        }
        final Interet interet = getItem(position);
        if (interet.getImageURL() != null) {
            Glide.with(getContext()).load(interet.getImageURL()).into(viewHolder.image);
        }
        viewHolder.nom.setText(interet.getNom());
        final InteretViewHolder finalViewHolder = viewHolder;
        if (((ChoixInteretsActivity)getContext()).estSelectionne(interet.getId())) {
            finalViewHolder.image.setBackgroundColor(getContext().getResources().getColor(R.color.blue));
            finalViewHolder.nom.setTextColor(getContext().getResources().getColor(R.color.blue));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext().getClass().equals(ChoixInteretsActivity.class)) {
                    if (((ChoixInteretsActivity)getContext()).estSelectionne(interet.getId())) {
                        finalViewHolder.image.setBackgroundColor(Color.TRANSPARENT);
                        finalViewHolder.nom.setTextColor(getContext().getResources().getColor(R.color.grey));
                        ((ChoixInteretsActivity)getContext()).deselectInteret(interet.getId());
                    }
                    else {
                        finalViewHolder.image.setBackgroundColor(getContext().getResources().getColor(R.color.blue));
                        finalViewHolder.nom.setTextColor(getContext().getResources().getColor(R.color.blue));
                        ((ChoixInteretsActivity)getContext()).selectInteret(interet.getId());
                    }
                }
            }
        });
        return convertView;
    }

    private class InteretViewHolder {
        public TextView nom;
        public ImageView image;
    }

}
