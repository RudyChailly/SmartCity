package com.example.smartcity.models.groupe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.smartcity.MainActivity;
import com.example.smartcity.R;
import com.example.smartcity.ui.chat.messages.MessageActivity;

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
            viewHolder.rejoint = (ImageView) convertView.findViewById(R.id.groupe_rejoint);
        }
        final Groupe groupe = getItem(position);
        viewHolder.nom.setText(groupe.getNom());
        if (groupe.getInteret() != null) {
            viewHolder.interet.setText(groupe.getInteret().toString());
        }
        if (groupe.estRejoint()) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), MessageActivity.class);
                    intent.putExtra("groupe_id", groupe.getId());
                    intent.putExtra("groupe_nom", groupe.getNom());
                    getContext().startActivity(intent);
                }
            });
            viewHolder.rejoint.setImageResource(R.drawable.ic_clear_black_24dp);
            viewHolder.rejoint.setColorFilter(ContextCompat.getColor(getContext(), R.color.red));
            viewHolder.rejoint.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle(getContext().getResources().getString(R.string.quitter) + " " + groupe.getNom() + " ?");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton(getContext().getResources().getString(R.string.oui), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            groupe.quitter();
                            ((MainActivity) getContext()).requestGroupeQuitter(groupe);
                            Toast.makeText(getContext(), groupe.getNom() + " " + getContext().getResources().getString(R.string.quitté), Toast.LENGTH_SHORT).show();
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
            });
        }
        else {
            viewHolder.rejoint.setImageResource(R.drawable.ic_add_black_24dp);
            viewHolder.rejoint.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle(getContext().getResources().getString(R.string.rejoindre) + " " + groupe.getNom() + " ?");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton(getContext().getResources().getString(R.string.oui), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            groupe.rejoindre();
                            ((MainActivity) getContext()).requestGroupeRejoindre(groupe);
                            Toast.makeText(getContext(), groupe.getNom() + " " + getContext().getResources().getString(R.string.rejoint), Toast.LENGTH_SHORT).show();
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
            });
        }

        return convertView;
    }

    private class GroupeViewHolder {
        public TextView nom;
        public TextView ville;
        public TextView interet;
        public TextView message;
        public ImageView rejoint;
    }

}
