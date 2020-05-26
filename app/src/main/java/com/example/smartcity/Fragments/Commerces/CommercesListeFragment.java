package com.example.smartcity.Fragments.Commerces;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.smartcity.Activities.MainActivity;
import com.example.smartcity.R;
import com.example.smartcity.Models.Utilisateur;
import com.example.smartcity.Models.Commerce;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommercesListeFragment extends Fragment {

    private ArrayList<Commerce> commerces;
    private ListView listView_commerces;

    public CommercesListeFragment(){
        commerces = new ArrayList<Commerce>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commerces_liste, container, false);
        listView_commerces = view.findViewById(R.id.liste_commerces);
        if (((MainActivity)getActivity()).getUtilisateur() == null) {
            ((MainActivity)getActivity()).getReferenceUtilisateurs().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ((MainActivity)getActivity()).setUtilisateur(dataSnapshot.getValue(Utilisateur.class));
                    ((MainActivity)getActivity()).requestCommercesUtilisateur();
                }

                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
        else {
            ((MainActivity)getActivity()).requestCommercesUtilisateur();
        }
        listView_commerces.setAdapter(((MainActivity)getActivity()).getCommerceAdapter());
        return view;
    }

}
