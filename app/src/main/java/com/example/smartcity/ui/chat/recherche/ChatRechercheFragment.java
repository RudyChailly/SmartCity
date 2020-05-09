package com.example.smartcity.ui.chat.recherche;

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
import com.example.smartcity.models.groupe.Groupe;
import com.example.smartcity.models.groupe.GroupeAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            ((MainActivity)getActivity()).getReferenceUtilisateurs().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Utilisateur utilisateur = dataSnapshot.child("0").getValue(Utilisateur.class);
                    ((MainActivity)getActivity()).setUtilisateur(utilisateur);
                    ((MainActivity)getActivity()).getUtilisateur().setId(Integer.parseInt(dataSnapshot.child("0").getKey()));
                    ((MainActivity)getActivity()).getUtilisateur().checkArrayList();
                    ((MainActivity)getActivity()).requestGroupesInteret();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            ((MainActivity)getActivity()).requestGroupesInteret();
        }
        listView_groupes.setAdapter(((MainActivity)getActivity()).getGroupeInteretAdapter());
        return view;
    }

}