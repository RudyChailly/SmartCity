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
    private OffreAdapter offreAdapter;
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
        listView_offres = (ListView) view.findViewById(R.id.liste_offres);
        offreAdapter = new OffreAdapter(getContext().getApplicationContext(), offres);
        listView_offres.setAdapter(offreAdapter);
        getOffres();
        return view;
    }

    public void getOffres() {
        ArrayList<Offre> offresRecuperees = ((MainActivity)getActivity()).getOffres();
        if (offresRecuperees == null) {
            offres = new ArrayList<Offre>();
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(requestAllCommerces());
            queue.add(requestOffres());
        }
        else {
            if (offreAdapter.getCount() == 0) {
                this.offres = offresRecuperees;
                offreAdapter.addAll(offresRecuperees);
                offreAdapter.notifyDataSetChanged();
            }
        }
    }

    public JsonArrayRequest requestOffres() {
        String url = "http://10.0.2.2:8888/utilisateurs/0/offres";
        JsonArrayRequest offresRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Offre offre = new Offre(jsonObject.getString("intitule"), jsonObject.getString("description"), jsonObject.getString("date"), jsonObject.getDouble("prix"));
                                offre.setCommerce(jsonObject.getInt("commerce"), ((MainActivity)getActivity()).getAllCommerces());
                                offreAdapter.add(offre);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        offreAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERREUR", error.toString());
                    }
                }
        );
        return offresRequest;
    }

    public JsonArrayRequest requestAllCommerces() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://10.0.2.2:8888/commerces";

        JsonArrayRequest commercesRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Commerce commerce = new Commerce(jsonObject.getInt("id"), jsonObject.getString("nom"), jsonObject.getString("adresse"));
                                commerces.add(commerce);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERREUR", error.toString());
                    }
                }
        );
        return commercesRequest;
    }

}