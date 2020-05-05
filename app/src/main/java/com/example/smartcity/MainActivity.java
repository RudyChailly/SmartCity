package com.example.smartcity;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartcity.models.commerce.CommerceAdapter;
import com.example.smartcity.models.commerce.offre.OffreAdapter;
import com.example.smartcity.models.groupe.Groupe;
import com.example.smartcity.models.Interet;
import com.example.smartcity.models.actualite.Actualite;
import com.example.smartcity.models.commerce.Commerce;
import com.example.smartcity.models.commerce.offre.Offre;
import com.example.smartcity.models.Ville;
import com.example.smartcity.models.groupe.GroupeAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Actualite> actualites;
    private ArrayList<Commerce> commercesUtilisateur, allCommerces;
    private ArrayList<Offre> offresUtilisateur;
    private ArrayList<Interet> allInterets;
    private ArrayList<Ville> allVilles;
    private ArrayList<Groupe> groupesUtilisateur, groupesInterets;

    private OffreAdapter offreAdapter;
    private CommerceAdapter commerceAdapter;
    private GroupeAdapter groupeUtilisateurAdapter, groupeInteretsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        RequestQueue queue = Volley.newRequestQueue(this);

        if (allInterets == null) {
            queue.add(requestAllInterets());
        }
        if (allVilles == null) {
            queue.add(requestAllVilles());
        }
        if (allCommerces == null) {
            queue.add(requestAllCommerces());
        }

    }

    /********************** VILLES **********************/
    public JsonArrayRequest requestAllVilles() {
        String url = "http://10.0.2.2:8888/villes";
        this.allVilles = new ArrayList<Ville>();
        JsonArrayRequest villesRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Ville ville = new Ville(jsonObject.getInt("id"), jsonObject.getString("nom"), jsonObject.getString("code"));
                                allVilles.add(ville);
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
        return villesRequest;
    }

    public ArrayList<Ville> getAllVilles() {
        return allVilles;
    }

    /********************** INTERETS **********************/
    public JsonArrayRequest requestAllInterets() {
        String url = "http://10.0.2.2:8888/interets";
        this.allInterets = new ArrayList<Interet>();
        JsonArrayRequest interetsRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Interet interet = new Interet(jsonObject.getInt("id"), jsonObject.getString("nom"));
                                allInterets.add(interet);
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
        return interetsRequest;
    }

    public ArrayList<Interet> getAllInterets() {
        return allInterets;
    }

    /********************** ACTUALITES **********************/
    public ArrayList<Actualite> getActualites() {
        return actualites;
    }

    public void setActualites(ArrayList<Actualite> actualites) {
        this.actualites = actualites;
    }

    /********************** COMMERCES **********************/
    public void generateCommercesUtilisateur() {
        if (commercesUtilisateur == null) {
            commercesUtilisateur = new ArrayList<Commerce>();
            commerceAdapter = new CommerceAdapter(this, commercesUtilisateur);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(requestCommercesSuivis());
            queue.add(requestCommercesInterets());
        }
        else {
            if (commerceAdapter.getCount() == 0) {
                commerceAdapter.addAll(commercesUtilisateur);
                commerceAdapter.notifyDataSetChanged();
            }
        }
    }

    public CommerceAdapter getCommerceAdapter() {
        return commerceAdapter;
    }

    public JsonArrayRequest requestCommercesSuivis() {
        String url = "http://10.0.2.2:8888/commerces/utilisateur/0";
        JsonArrayRequest commercesRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Commerce commerce = new Commerce(jsonObject.getInt("id"), jsonObject.getString("nom"), jsonObject.getString("adresse"));
                                commerce.abonner();
                                commerce.setVille(jsonObject.getInt("ville"), allVilles);
                                commerce.setInteret(jsonObject.getInt("interet"), allInterets);
                                commerceAdapter.add(commerce);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        commerceAdapter.notifyDataSetChanged();
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
    public JsonArrayRequest requestCommercesInterets() {
        String url = "http://10.0.2.2:8888/commerces/utilisateur/0/interets";
        JsonArrayRequest commercesRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Commerce commerce = new Commerce(jsonObject.getInt("id"), jsonObject.getString("nom"), jsonObject.getString("adresse"));
                                commerce.setVille(jsonObject.getInt("ville"), allVilles);
                                commerce.setInteret(jsonObject.getInt("interet"), allInterets);
                                commerceAdapter.add(commerce);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        commerceAdapter.notifyDataSetChanged();
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
    public JsonArrayRequest requestAllCommerces() {
        String url = "http://10.0.2.2:8888/commerces";
        this.allCommerces = new ArrayList<Commerce>();
        JsonArrayRequest commercesRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Commerce commerce = new Commerce(jsonObject.getInt("id"), jsonObject.getString("nom"), jsonObject.getString("adresse"));
                                allCommerces.add(commerce);
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

    public void requestCommerceAbonner(final Commerce commerce) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8888/commerces/utilisateur/0/abonner/"+commerce.getId();
        JsonObjectRequest abonnerRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("result")) {
                                if (!commerceAdapter.contains(commerce)) {
                                    commerceAdapter.add(commerce);
                                    commerceAdapter.notifyDataSetChanged();
                                    refreshOffres();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
        queue.add(abonnerRequest);
        queue.add(refreshOffres());
    }
    public void requestCommerceDesabonner(final Commerce commerce) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8888/commerces/utilisateur/0/desabonner/"+commerce.getId();
        JsonObjectRequest abonnerRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("result")) {
                                if (!commerceAdapter.contains(commerce)) {
                                    commerceAdapter.remove(commerce);
                                    commerceAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
        queue.add(abonnerRequest);
        queue.add(refreshOffres());
    }

    /********************** OFFRES **********************/
    public void generateOffresUtilisateur() {
        if (offresUtilisateur == null) {
            offresUtilisateur = new ArrayList<Offre>();
            offreAdapter = new OffreAdapter(this, offresUtilisateur);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(requestAllCommerces());
            queue.add(requestOffres());
        }
        else {
            if (offreAdapter.getCount() == 0) {
                offreAdapter.addAll(offresUtilisateur);
                offreAdapter.notifyDataSetChanged();
            }
        }
    }

    public OffreAdapter getOffreAdapter() {
        return offreAdapter;
    }

    public JsonArrayRequest requestOffres() {
        String url = "http://10.0.2.2:8888/offres/utilisateur/0";
        JsonArrayRequest offresRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Offre offre = new Offre(jsonObject.getString("intitule"), jsonObject.getString("description"), jsonObject.getString("date"), jsonObject.getDouble("prix"));
                                offre.setCommerce(jsonObject.getInt("commerce"), allCommerces);
                                Log.d("Offre", offre.getIntitule());
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
    public JsonArrayRequest refreshOffres() {
        offreAdapter.clear();
        return requestOffres();
    }

    /********************** GROUPES **********************/
    public void generateGroupesUtilisateur() {
        if (groupesUtilisateur == null) {
            groupesUtilisateur = new ArrayList<Groupe>();
            groupeUtilisateurAdapter = new GroupeAdapter(this, groupesUtilisateur);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(requestGroupesUtilisateur());
        }
        else {
            if (groupeUtilisateurAdapter.getCount() == 0) {
                groupeUtilisateurAdapter.addAll(groupesUtilisateur);
                groupeUtilisateurAdapter.notifyDataSetChanged();
            }
        }
    }
    public void generateGroupesInterets() {
        if (groupesInterets == null) {
            groupesInterets = new ArrayList<Groupe>();
            groupeInteretsAdapter = new GroupeAdapter(this, groupesInterets);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(requestGroupesInteret());
        }
        else {
            if (groupeInteretsAdapter.getCount() == 0) {
                groupeInteretsAdapter.addAll(groupesInterets);
                groupeInteretsAdapter.notifyDataSetChanged();
            }
        }
    }

    public GroupeAdapter getGroupeUtilisateurAdapter(){
        return groupeUtilisateurAdapter;
    }
    public GroupeAdapter getGroupeInteretAdapter(){
        return groupeInteretsAdapter;
    }

    public JsonArrayRequest requestGroupesUtilisateur() {
        String url = "http://10.0.2.2:8888/groupes/utilisateur/0";
        this.groupesUtilisateur = new ArrayList<Groupe>();
        JsonArrayRequest interetsRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Groupe groupe = new Groupe(jsonObject.getInt("id"), jsonObject.getString("nom"));
                                groupe.rejoindre();
                                groupe.setInteret(jsonObject.getInt("interet"),  allInterets);
                                groupe.setVille(jsonObject.getInt("ville"), allVilles);
                                groupeUtilisateurAdapter.add(groupe);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        groupeUtilisateurAdapter.notifyDataSetChanged();
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
    public JsonArrayRequest requestGroupesInteret() {
        String url = "http://10.0.2.2:8888/groupes/utilisateur/0/interets";
        this.groupesInterets = new ArrayList<Groupe>();
        JsonArrayRequest interetsRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Groupe groupe = new Groupe(jsonObject.getInt("id"), jsonObject.getString("nom"));
                                groupe.setInteret(jsonObject.getInt("interet"),  allInterets);
                                groupe.setVille(jsonObject.getInt("ville"), allVilles);
                                groupeInteretsAdapter.add(groupe);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        groupeInteretsAdapter.notifyDataSetChanged();
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
