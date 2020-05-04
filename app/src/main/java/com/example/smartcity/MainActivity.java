package com.example.smartcity;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartcity.models.groupe.Groupe;
import com.example.smartcity.models.Interet;
import com.example.smartcity.models.actualite.Actualite;
import com.example.smartcity.models.commerce.Commerce;
import com.example.smartcity.models.commerce.offre.Offre;
import com.example.smartcity.models.Ville;
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
    private ArrayList<Groupe> groupesUtilisateur, groupesARejoindre;

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

    /********************** ACTUALITES **********************/
    public ArrayList<Actualite> getActualites() {
        return actualites;
    }

    public void setActualites(ArrayList<Actualite> actualites) {
        this.actualites = actualites;
    }

    /********************** COMMERCES **********************/
    public ArrayList<Commerce> getCommercesUtilisateur() {
        return commercesUtilisateur;
    }

    public void setCommercesUtilisateurs(ArrayList<Commerce> commerces) {
        this.commercesUtilisateur = commerces;
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

    public ArrayList<Commerce> getAllCommerces() {
        return allCommerces;
    }

    /********************** OFFRES **********************/
    public ArrayList<Offre> getOffresUtilisateur() {
        return offresUtilisateur;
    }

    public void setOffresUtilisateur(ArrayList<Offre> offres) {
        this.offresUtilisateur = offres;
    }

    /********************** VILLES **********************/
    public ArrayList<Ville> getAllVilles() {
        return allVilles;
    }

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

    /********************** INTERETS **********************/
    public ArrayList<Interet> getAllInterets() {
        return allInterets;
    }

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

    /********************** GROUPES **********************/
    public ArrayList<Groupe> getGroupesUtilisateur() {
        return groupesUtilisateur;
    }

    public void setGroupesUtilisateurs(ArrayList<Groupe> groupes) {
        this.groupesUtilisateur = groupes;
    }

    public ArrayList<Groupe> getGroupesARejoindre() {
        return groupesARejoindre;
    }

    public void setGroupesARejoindre(ArrayList<Groupe> groupes) {
        this.groupesARejoindre = groupes;
    }

}
