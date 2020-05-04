package com.example.smartcity.ui.chat.recherche;

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
import com.example.smartcity.models.groupe.Groupe;
import com.example.smartcity.models.groupe.GroupeAdapter;

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
        View view = inflater.inflate(R.layout.fragment_chat_groupes, container, false);
        listView_groupes = view.findViewById(R.id.liste_groupes);
        groupeAdapter = new GroupeAdapter(getContext().getApplicationContext(), groupes);
        listView_groupes.setAdapter(groupeAdapter);
        getGroupes();
        return view;
    }

    public void getGroupes() {
        ArrayList<Groupe> groupesRecuperes = ((MainActivity)getActivity()).getGroupesARejoindre();
        if (groupesRecuperes == null) {
            groupes = new ArrayList<Groupe>();
            RequestQueue queue = Volley.newRequestQueue(getContext());
            if (((MainActivity) getActivity()).getAllInterets() == null) {
                queue.add(((MainActivity) getActivity()).requestAllInterets());
            }
            if (((MainActivity) getActivity()).getAllVilles() == null) {
                queue.add(((MainActivity) getActivity()).requestAllVilles());
            }
            queue.add(requestGroupesARejoindre());
        }
        else {
            if (groupeAdapter.getCount() == 0) {
                this.groupes = groupesRecuperes;
                groupeAdapter.addAll(groupesRecuperes);
                groupeAdapter.notifyDataSetChanged();
            }
        }
    }

    public JsonArrayRequest requestGroupesARejoindre() {
        String url = "http://10.0.2.2:8888/utilisateurs/0/search-groupes";
        this.groupes = new ArrayList<Groupe>();
        JsonArrayRequest interetsRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Groupe groupe = new Groupe(jsonObject.getInt("id"), jsonObject.getString("nom"));
                                groupe.setInteret(jsonObject.getInt("interet"),  ((MainActivity)getActivity()).getAllInterets());
                                groupe.setVille(jsonObject.getInt("ville"), ((MainActivity)getActivity()).getAllVilles());
                                groupeAdapter.add(groupe);
                                groupes.add(groupe);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        groupeAdapter.notifyDataSetChanged();
                        ((MainActivity) getActivity()).setGroupesARejoindre(groupes);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERREUR", error.toString());
                    }
                }
        );
        return interetsRequest;
    }

}