package com.example.smartcity.ui.chat.groupes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.smartcity.MainActivity;
import com.example.smartcity.R;
import com.example.smartcity.models.Utilisateur;
import com.example.smartcity.models.actualite.ActualiteAdapter;
import com.example.smartcity.models.groupe.Groupe;
import com.example.smartcity.models.groupe.GroupeAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatGroupesFragment extends Fragment {

    private ArrayList<Groupe> groupes;
    private ListView listView_groupes;

    public ChatGroupesFragment(){
        groupes = new ArrayList<Groupe>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_groupes, container, false);
        listView_groupes = view.findViewById(R.id.liste_groupes);
        if (((MainActivity)getActivity()).getUtilisateur() == null) {
            ((MainActivity)getActivity()).getReferenceUtilisateurs().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ((MainActivity)getActivity()).setUtilisateur(dataSnapshot.getValue(Utilisateur.class));
                    ((MainActivity)getActivity()).requestGroupesUtilisateur();
                }

                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
        else {
            ((MainActivity)getActivity()).requestGroupesUtilisateur();
        }
        listView_groupes.setAdapter(((MainActivity)getActivity()).getGroupeUtilisateurAdapter());

        view.findViewById(R.id.bouton_nouv_groupe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creationGroupe();
            }
        });

        return view;
    }

    public void creationGroupe() {
        Intent creationGroupeIntent = new Intent(getContext(), CreationGroupe.class);
        startActivity(creationGroupeIntent);
    }



}