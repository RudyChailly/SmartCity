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
import com.example.smartcity.R;
import com.example.smartcity.models.actualite.Actualite;
import com.example.smartcity.models.actualite.ActualiteAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActualitesNewsFragment extends Fragment {

    private ArrayList<Actualite> actualites;
    private ListView listView_actualites;
    private  ActualiteAdapter actualiteAdapter;

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
        listView_actualites = (ListView) view.findViewById(R.id.liste_actualites);

        actualiteAdapter = new ActualiteAdapter(getContext().getApplicationContext(), actualites);

        listView_actualites.setAdapter(actualiteAdapter);

        getActualites();
        return view;
    }

    public void getActualites() {
        actualites = new ArrayList<Actualite>();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://10.0.2.2:8888/actualites";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Actualite actualite = new Actualite(jsonObject.getString("titre"), jsonObject.getString("url"),  jsonObject.getString("source"), jsonObject.getString("date"));
                                actualiteAdapter.add(actualite);

                                Log.d("Actualite n°"+i, actualite.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //actualiteAdapter.addAll(actualites);
                        actualiteAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERREUR", error.toString());
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }

}
