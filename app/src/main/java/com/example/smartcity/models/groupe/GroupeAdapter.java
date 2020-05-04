package com.example.smartcity.models.groupe;

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

import java.util.List;

public class GroupeAdapter extends ArrayAdapter<Groupe> {

    public GroupeAdapter(Context context, List<Groupe> groupes) {
        super(context, 0, groupes);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.groupe, parent, false);
        }
        GroupeViewHolder viewHolder = (GroupeViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new GroupeViewHolder();
            viewHolder.nom = (TextView)convertView.findViewById(R.id.groupe_nom);
            viewHolder.interet = (TextView)convertView.findViewById(R.id.groupe_interet);
            viewHolder.groupe_rejoint = (ImageView) convertView.findViewById(R.id.groupe_rejoint);
        }
        Groupe groupe = getItem(position);
        viewHolder.nom.setText(groupe.getNom());
        viewHolder.interet.setText(groupe.getInteret().toString());
        if (groupe.estRejoint()) {
            viewHolder.groupe_rejoint.setVisibility(View.INVISIBLE);
        }
        // TODO: viewHolder.suivi

        return convertView;
    }

    private class GroupeViewHolder {
        public TextView nom;
        public TextView ville;
        public TextView interet;
        public TextView message;
        public ImageView groupe_rejoint;
    }

}
