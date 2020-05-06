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
import com.example.smartcity.models.actualite.ActualiteAdapter;
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

    private ArrayList<Actualite> actualitesUtilisateur;
    private ArrayList<Commerce> commercesUtilisateur, allCommerces;
    private ArrayList<Offre> offresUtilisateur;

    private ArrayList<Groupe> groupesUtilisateur, groupesInterets;

    private ActualiteAdapter actualiteAdapter;
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

    }

    /******************************************** ACTUALITES ********************************************/
    public void generateActualitesUtilisateur() {
        if (actualitesUtilisateur == null) {
            actualitesUtilisateur = new ArrayList<Actualite>();
            actualiteAdapter = new ActualiteAdapter(this, actualitesUtilisateur);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(requestActualites());
        }
        else {
            if (actualiteAdapter.getCount() == 0) {
                actualiteAdapter.addAll(actualitesUtilisateur);
                actualiteAdapter.notifyDataSetChanged();
            }
        }
    }

    public ActualiteAdapter getActualiteAdapter() {
        return actualiteAdapter;
    }

    public JsonArrayRequest requestActualites() {
        String url = "http://10.0.2.2:8888/actualites/utilisateur/0";
        JsonArrayRequest actualitesRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonActualite = response.getJSONObject(i);
                                Actualite actualite = new Actualite(jsonActualite);
                                JSONObject jsonInteret = jsonActualite.getJSONObject("interet");
                                Interet interet = new Interet(jsonInteret);
                                actualite.setInteret(interet);
                                actualiteAdapter.add(actualite);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
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
        return actualitesRequest;
    }
    /******************************************** COMMERCES ********************************************/
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
                                JSONObject jsonCommerce = response.getJSONObject(i);
                                Commerce commerce = new Commerce(jsonCommerce);
                                commerce.abonner();

                                JSONObject jsonInteret = jsonCommerce.getJSONObject("interet");
                                Interet interet = new Interet(jsonInteret);
                                JSONObject jsonVille = jsonCommerce.getJSONObject("ville");
                                Ville ville = new Ville(jsonVille);

                                commerce.setInteret(interet);
                                commerce.setVille(ville);

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
                                JSONObject jsonCommerce = response.getJSONObject(i);
                                Commerce commerce = new Commerce(jsonCommerce);

                                JSONObject jsonInteret = jsonCommerce.getJSONObject("interet");
                                Interet interet = new Interet(jsonInteret);
                                JSONObject jsonVille = jsonCommerce.getJSONObject("ville");
                                Ville ville = new Ville(jsonVille);

                                commerce.setInteret(interet);
                                commerce.setVille(ville);
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

    public void requestCommerceAbonner(final Commerce commerce) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8888/commerces/utilisateur/0/abonner/"+commerce.getId();
        JsonObjectRequest abonnerRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {}
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERREUR", error.toString());
                    }
                }
        );
        queue.add(abonnerRequest);
        queue.add(refreshOffresAbonner(commerce));
    }
    public void requestCommerceDesabonner(final Commerce commerce) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8888/commerces/utilisateur/0/desabonner/"+commerce.getId();
        boolean refresh = false;
        JsonObjectRequest desabonnerRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {}
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERREUR", error.toString());
                    }
                }
        );
        queue.add(desabonnerRequest);
        queue.add(refreshOffresDesabonner(commerce));
    }

    /******************************************** OFFRES ********************************************/
    public void generateOffresUtilisateur() {
        if (offresUtilisateur == null) {
            offresUtilisateur = new ArrayList<Offre>();
            offreAdapter = new OffreAdapter(this, offresUtilisateur);
            RequestQueue queue = Volley.newRequestQueue(this);
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
                                JSONObject jsonOffre = response.getJSONObject(i);
                                Offre offre = new Offre(jsonOffre);

                                JSONObject jsonCommerce = jsonOffre.getJSONObject("commerce");
                                Commerce commerce = new Commerce(jsonCommerce);

                                offre.setCommerce(commerce);
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

    public JsonArrayRequest refreshOffresAbonner(final Commerce commerce) {
        String url = "http://10.0.2.2:8888/commerces/"+commerce.getId()+"/offres";
        JsonArrayRequest offresRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Offre offre = new Offre(jsonObject.getString("_id"), jsonObject.getString("intitule"), jsonObject.getString("description"), jsonObject.getString("date"), jsonObject.getDouble("prix"));
                                offre.setCommerce(commerce);
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
    public JsonArrayRequest refreshOffresDesabonner(final Commerce commerce) {
        String url = "http://10.0.2.2:8888/commerces/"+commerce.getId()+"/offres";
        JsonArrayRequest offresRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Offre offre = new Offre(jsonObject.getString("_id"), jsonObject.getString("intitule"), jsonObject.getString("description"), jsonObject.getString("date"), jsonObject.getDouble("prix"));
                                offreAdapter.removeOffre(offre);
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

    /******************************************** GROUPES ********************************************/
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
                                JSONObject jsonnGroupe = response.getJSONObject(i);
                                Groupe groupe = new Groupe(jsonnGroupe);
                                groupe.rejoindre();

                                JSONObject jsonInteret = jsonnGroupe.getJSONObject("interet");
                                Interet interet = new Interet(jsonInteret);
                                JSONObject jsonVille = jsonnGroupe.getJSONObject("ville");
                                Ville ville = new Ville(jsonVille);

                                groupe.setInteret(interet);
                                groupe.setVille(ville);
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
                                JSONObject jsonnGroupe= response.getJSONObject(i);
                                Groupe groupe = new Groupe(jsonnGroupe);

                                JSONObject jsonInteret = jsonnGroupe.getJSONObject("interet");
                                Interet interet = new Interet(jsonInteret);
                                JSONObject jsonVille = jsonnGroupe.getJSONObject("ville");
                                Ville ville = new Ville(jsonVille);

                                groupe.setInteret(interet);
                                groupe.setVille(ville);
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

    public void requestGroupeRejoindre(final Groupe groupe) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8888/groupes/utilisateur/0/rejoindre/"+groupe.getId();
        JsonObjectRequest rejoindreRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("result")) {
                                    groupeUtilisateurAdapter.add(groupe);
                                    groupeUtilisateurAdapter.notifyDataSetChanged();
                                    groupeInteretsAdapter.remove(groupe);
                                    groupeInteretsAdapter.notifyDataSetChanged();
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
        queue.add(rejoindreRequest);
    }
    public void requestGroupeQuitter(final Groupe groupe) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8888/groupes/utilisateur/0/quitter/"+groupe.getId();
        JsonObjectRequest quitterRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("result")) {
                                groupeUtilisateurAdapter.remove(groupe);
                                groupeUtilisateurAdapter.notifyDataSetChanged();
                                groupeInteretsAdapter.add(groupe);
                                groupeInteretsAdapter.notifyDataSetChanged();
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
        queue.add(quitterRequest);
    }

}
