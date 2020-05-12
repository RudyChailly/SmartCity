package com.example.smartcity.ui.chat.recherche;

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
import com.example.smartcity.models.groupe.Groupe;
import com.example.smartcity.models.groupe.GroupeAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatRechercheFragment extends Fragment {

    private ArrayList<Groupe> groupes;
    private ListView listView_groupes;
    private GroupeAdapter groupeAdapter;

    public ChatRechercheFragment(){
        groupes = new ArrayList<Groupe>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_recherche, container, false);
        listView_groupes = view.findViewById(R.id.liste_groupes);
        if (((MainActivity)getActivity()).getUtilisateur() == null) {
            ((MainActivity)getActivity()).getReferenceUtilisateurs().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ((MainActivity)getActivity()).setUtilisateur(dataSnapshot.getValue(Utilisateur.class));
                    ((MainActivity)getActivity()).requestGroupesInteret();
                }

                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
        else {
            ((MainActivity)getActivity()).requestGroupesInteret();
        }
        listView_groupes.setAdapter(((MainActivity)getActivity()).getGroupeInteretAdapter());
        return view;
    }

}