package com.example.smartcity.ui.commerces.offres;

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
import com.example.smartcity.models.commerce.Commerce;
import com.example.smartcity.models.commerce.offre.Offre;
import com.example.smartcity.models.commerce.offre.OffreAdapter;

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
        ((MainActivity)getActivity()).generateOffresUtilisateur();
        listView_offres.setAdapter(((MainActivity)getActivity()).getOffreAdapter());

        return view;
    }

}