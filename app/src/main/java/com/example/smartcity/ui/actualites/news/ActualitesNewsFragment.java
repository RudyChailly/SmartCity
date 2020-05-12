package com.example.smartcity.ui.actualites.news;

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
import com.example.smartcity.models.actualite.Actualite;
import com.example.smartcity.models.actualite.ActualiteAdapter;
import com.example.smartcity.ui.commerces.CommercesFragment;
import com.example.smartcity.ui.commerces.liste.CommercesListeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ActualitesNewsFragment extends Fragment {

    private ArrayList<Actualite> actualites;
    private ListView listView_actualites;

    public ActualitesNewsFragment(){
        actualites = new ArrayList<Actualite>();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actualites_news, container, false);
        listView_actualites = view.findViewById(R.id.liste_actualites);
        if (((MainActivity)getActivity()).getUtilisateur() == null) {
            ((MainActivity)getActivity()).getReferenceUtilisateurs().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ((MainActivity)getActivity()).setUtilisateur(dataSnapshot.getValue(Utilisateur.class));
                    ((MainActivity)getActivity()).requestActualitesUtilisateur();
                }

                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
        else {
            ((MainActivity)getActivity()).requestActualitesUtilisateur();
        }
        listView_actualites.setAdapter(((MainActivity)getActivity()).getActualiteAdapter());
        return view;
    }





}
