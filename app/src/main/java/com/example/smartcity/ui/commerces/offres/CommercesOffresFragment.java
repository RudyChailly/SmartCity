package com.example.smartcity.ui.commerces.offres;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartcity.MainActivity;
import com.example.smartcity.R;
import com.example.smartcity.models.Utilisateur;
import com.example.smartcity.models.commerce.Commerce;
import com.example.smartcity.models.commerce.offre.Offre;
import com.example.smartcity.models.commerce.offre.OffreAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommercesOffresFragment extends Fragment {

    private ArrayList<Offre> offres;
    private ListView listView_offres;
    private ArrayList<Commerce> commerces;

    public CommercesOffresFragment(){
        offres = new ArrayList<Offre>();
        commerces = new ArrayList<Commerce>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commerces_offres, container, false);
        listView_offres = view.findViewById(R.id.liste_offres);
        if (((MainActivity)getActivity()).getUtilisateur() == null) {
            ((MainActivity)getActivity()).getReferenceUtilisateurs().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ((MainActivity)getActivity()).setUtilisateur(dataSnapshot.child("0").getValue(Utilisateur.class));
                    ((MainActivity)getActivity()).getUtilisateur().setId(Integer.parseInt(dataSnapshot.getKey()));
                    ((MainActivity)getActivity()).getUtilisateur().checkArrayList();
                    ((MainActivity)getActivity()).requestOffresUtilisateur();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            ((MainActivity)getActivity()).requestOffresUtilisateur();
        }
        listView_offres.setAdapter(((MainActivity)getActivity()).getOffreAdapter());

        return view;
    }

}