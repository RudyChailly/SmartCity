package com.example.smartcity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
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
    private ArrayList<Commerce> commerces, allCommerces;
    private ArrayList<Offre> offres;
    private ArrayList<Interet> interets;
    private ArrayList<Ville> villes;

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
        if (villes == null) {
            queue.add(requestAllVilles());
        }
        if (interets == null) {
            queue.add(requestAllInterets());
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
    public ArrayList<Commerce> getCommerces() {
        return commerces;
    }

    public void setCommerces(ArrayList<Commerce> commerces) {
        this.commerces = commerces;
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
    public ArrayList<Offre> getOffres() {
        return offres;
    }

    public void setOffres(ArrayList<Offre> offres) {
        this.offres = offres;
    }

    /********************** VILLES **********************/
    public ArrayList<Ville> getVilles() {
        return villes;
    }

    public JsonArrayRequest requestAllVilles() {
        String url = "http://10.0.2.2:8888/villes";
        this.villes = new ArrayList<Ville>();
        JsonArrayRequest villesRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Ville ville = new Ville(jsonObject.getInt("id"), jsonObject.getString("nom"), jsonObject.getString("code"));
                                villes.add(ville);
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
    public ArrayList<Interet> getInterets() {
        return interets;
    }

    public JsonArrayRequest requestAllInterets() {
        String url = "http://10.0.2.2:8888/interets";
        this.interets = new ArrayList<Interet>();
        JsonArrayRequest interetsRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Interet interet = new Interet(jsonObject.getInt("id"), jsonObject.getString("nom"));
                                interets.add(interet);
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

}
