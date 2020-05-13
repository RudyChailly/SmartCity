package com.example.smartcity.models.Interet;

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

import com.example.smartcity.R;
import com.example.smartcity.ui.demarrage.Interets;

import java.util.List;

public class InteretAdapter extends ArrayAdapter<Interet> {

    private Context context;

    public InteretAdapter(Context context, List<Interet> interets) {
        super(context, 0, interets);
    }

    public void addContext(Context context) {
        this.context = context;
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
        viewHolder.nom.setText(interet.getNom());
        final InteretViewHolder finalViewHolder = viewHolder;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.getClass().equals(Interets.class)) {
                    if (((Interets)context).estSelectionne(interet.getId())) {
                        finalViewHolder.image.setBackgroundColor(Color.TRANSPARENT);
                        finalViewHolder.nom.setTextColor(context.getResources().getColor(R.color.grey));
                        ((Interets)context).deselectInteret(interet.getId());
                    }
                    else {
                        finalViewHolder.image.setBackgroundColor(context.getResources().getColor(R.color.blue));
                        finalViewHolder.nom.setTextColor(context.getResources().getColor(R.color.blue));
                        ((Interets)context).selectInteret(interet.getId());
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