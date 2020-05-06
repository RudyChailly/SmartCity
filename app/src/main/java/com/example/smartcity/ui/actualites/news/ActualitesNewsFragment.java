package com.example.smartcity.ui.actualites.news;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartcity.MainActivity;
import com.example.smartcity.R;
import com.example.smartcity.models.actualite.Actualite;
import com.example.smartcity.models.actualite.ActualiteAdapter;
import com.example.smartcity.ui.commerces.CommercesFragment;
import com.example.smartcity.ui.commerces.liste.CommercesListeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        ((MainActivity)getActivity()).generateActualitesUtilisateur();
        listView_actualites.setAdapter(((MainActivity)getActivity()).getActualiteAdapter());
        return view;
    }





}
