package com.example.smartcity.ui.commerces.liste;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartcity.MainActivity;
import com.example.smartcity.R;
import com.example.smartcity.models.actualite.Actualite;
import com.example.smartcity.models.commerce.Commerce;
import com.example.smartcity.models.commerce.CommerceAdapter;
import com.example.smartcity.models.commerce.offre.Offre;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;

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
        listView_commerces = (ListView) view.findViewById(R.id.liste_commerces);
        ((MainActivity)getActivity()).generateCommercesUtilisateur();
        listView_commerces.setAdapter(((MainActivity)getActivity()).getCommerceAdapter());
        return view;
    }

}
