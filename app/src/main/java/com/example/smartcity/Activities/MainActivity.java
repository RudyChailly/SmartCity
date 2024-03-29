package com.example.smartcity.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartcity.R;
import com.example.smartcity.Models.Utilisateur;
import com.example.smartcity.Adapters.ActualiteAdapter;
import com.example.smartcity.Models.Meteo;
import com.example.smartcity.Adapters.CommerceAdapter;
import com.example.smartcity.Adapters.OffreAdapter;
import com.example.smartcity.Models.Groupe;
import com.example.smartcity.Models.Interet;
import com.example.smartcity.Models.Actualite;
import com.example.smartcity.Models.Commerce;
import com.example.smartcity.Models.Offre;
import com.example.smartcity.Models.Ville;
import com.example.smartcity.Adapters.GroupeAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static ArrayList actualitesUtilisateur;
    private static ArrayList commercesUtilisateur;
    private static ArrayList offresUtilisateur;

    private static ArrayList groupesUtilisateur, groupesInterets;

    private static ActualiteAdapter actualiteAdapter;
    private static OffreAdapter offreAdapter;
    private static CommerceAdapter commerceAdapter;
    private static GroupeAdapter groupeUtilisateurAdapter, groupeInteretsAdapter;

    private static Utilisateur utilisateur;



    private static DatabaseReference
            referenceActualites,
            referenceInterets,
            referenceCommerces,
            referenceOffres,
            referenceVilles,
            referenceUtilisateurs,
            referenceGroupes,
            referenceMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        setReferences(database);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        setAdapters();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Verifie que l'utilisateur est connecte
        if (firebaseUser != null) {
            requestUtilisateur(firebaseUser);
            if (savedInstanceState == null) {
                requestUtilisateur(firebaseUser);
            }
            else {
                if (savedInstanceState.containsKey("utilisateur")) {
                    utilisateur = savedInstanceState.getParcelable("utilisteur");
                }
                else {
                    requestUtilisateur(firebaseUser);
                }
                if (savedInstanceState.containsKey("meteo")) {
                    meteo = (Meteo)savedInstanceState.getSerializable("meteo");
                }
                if (savedInstanceState.containsKey("actualitesUtilisateur")) {
                    actualitesUtilisateur.clear();
                    actualitesUtilisateur.addAll(savedInstanceState.getParcelableArrayList("actualitesUtilisateur"));
                }
                if (savedInstanceState.containsKey("commercesUtilisateurs")) {
                    commercesUtilisateur.clear();
                    commercesUtilisateur.addAll(savedInstanceState.getParcelableArrayList("commercesUtilisateurs"));
                }
                if (savedInstanceState.containsKey("offresUtilisateur")) {
                    offresUtilisateur.clear();
                    offresUtilisateur.addAll(savedInstanceState.getParcelableArrayList("offresUtilisateur"));
                }
                if (savedInstanceState.containsKey("groupesUtilisateur")) {
                    groupesUtilisateur.clear();
                    groupesUtilisateur.addAll(savedInstanceState.getParcelableArrayList("groupesUtilisateur"));
                }
                if (savedInstanceState.containsKey("groupesInterets")) {
                    groupesInterets.clear();
                    groupesInterets.addAll(savedInstanceState.getParcelableArrayList("groupesInterets"));
                }
            }
        }
        else {
            Intent intent = new Intent(MainActivity.this, DemarrageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (utilisateur != null) {
            savedInstanceState.putSerializable("utilisateur", utilisateur);
        }
        if (meteo != null) {
            savedInstanceState.putSerializable("meteo", meteo);
        }
        if (actualitesUtilisateur != null) {
            savedInstanceState.putParcelableArrayList("actualitesUtilisateur", actualitesUtilisateur);
        }
        if (commercesUtilisateur != null) {
            savedInstanceState.putParcelableArrayList("commercesUtilisateur", commercesUtilisateur);
        }
        if (offresUtilisateur != null) {
            savedInstanceState.putParcelableArrayList("offresUtilisateur", offresUtilisateur);
        }
        if (groupesUtilisateur != null) {
            savedInstanceState.putParcelableArrayList("groupesUtilisateur", groupesUtilisateur);
        }
        if (groupesInterets != null) {
            savedInstanceState.putParcelableArrayList("groupesInterets", groupesInterets);
        }
    }

    /******************************************** METEO ********************************************/
    static final String  METEO_API_URL = "https://api.weatherbit.io/v2.0/current?";
    static final String  METEO_API_KEY = "917505d4cbd4452cbf041818779d6a80";

    Meteo meteo;
    double latitude, longitude;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView view_meteo_ville, view_meteo_temperature, view_meteo_description ;
    ImageView view_meteo_image;

    public void requestMeteo() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = METEO_API_URL+"lat="+latitude+"&lon="+longitude+"&key="+METEO_API_KEY;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonMeteo = new JSONObject(response).getJSONArray("data").getJSONObject(0);
                            String meteo_ville = jsonMeteo.getString("city_name");
                            int meteo_temperature = jsonMeteo.getInt("temp");
                            String meteo_code = jsonMeteo.getJSONObject("weather").getString("code");
                            meteo = new Meteo(getResources(), getPackageName(), meteo_temperature, meteo_ville, meteo_code);

                            affichageMeteo();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                });

        queue.add(stringRequest);
    }
    public void affichageMeteo() {
        if (meteo != null) {
            if (meteo.getVille() != null) {
                view_meteo_ville.setText(meteo.getVille());
            }
            if (meteo.getDescription() != null) {
                view_meteo_description.setText(meteo.getDescription());
            }
            view_meteo_temperature.setText(meteo.getTemperature() + " °C");
            if (meteo.getImage() > 0) {
                view_meteo_image.setImageResource(meteo.getImage());
            }
        }
    }
    public void requestLocation(TextView view_meteo_ville, ImageView view_meteo_image, TextView view_meteo_temperature, TextView view_meteo_description) {
        this.view_meteo_ville = view_meteo_ville;
        this.view_meteo_image = view_meteo_image;
        this.view_meteo_temperature = view_meteo_temperature;
        this.view_meteo_description = view_meteo_description;
        if (meteo != null) {
            affichageMeteo();
        }
        else {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }
    }
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    requestMeteo();
                }
            }
        });
    }

    /******************************************** UTILISATEURS ********************************************/


    public static void setUtilisateur(Utilisateur value) {
        utilisateur = value;
    }
    public static Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public static DatabaseReference getReferenceUtilisateurs() {
        return referenceUtilisateurs;
    }
    public void requestUtilisateur(FirebaseUser firebaseUser) {
        referenceUtilisateurs.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                utilisateur = dataSnapshot.getValue(Utilisateur.class);
                if (utilisateur.getIdInterets().size() == 0) {
                    finish();
                    Intent intent = new Intent(MainActivity.this, ChoixInteretsActivity.class);
                    intent.putExtra("redirectToMainActivity", true);
                    startActivity(intent);
                }
                else {
                    BottomNavigationView navView = findViewById(R.id.nav_view);
                    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
                    NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                    NavigationUI.setupWithNavController(navView, navController);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    /******************************************** ACTUALITES ********************************************/
    public static void requestActualitesUtilisateur() {
        if (actualiteAdapter.getCount() == 0 && actualitesUtilisateur.size() == 0) {
            referenceActualites.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final Actualite actualite = snapshot.getValue(Actualite.class);
                        actualite.setId(snapshot.getKey());
                        if (!(actualiteAdapter.contains(actualite)) && utilisateur.estInteresse(actualite)) {
                            referenceInterets.child(actualite.getIdInteret() + "").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Interet interet = dataSnapshot.getValue(Interet.class);
                                    if (interet != null) {
                                        actualite.setInteret(interet);
                                        if (!actualiteAdapter.contains(actualite)) {
                                            actualiteAdapter.add(actualite);
                                            actualiteAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }

    public static ActualiteAdapter getActualiteAdapter() {
        return actualiteAdapter;
    }

    /******************************************** COMMERCES ********************************************/
    public static void requestCommercesUtilisateur() {
        if (commerceAdapter.getCount() == 0 && commercesUtilisateur.size() == 0) {
            referenceCommerces.orderByChild("nom").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshotCommerces) {
                    for (final DataSnapshot snapshotCommerce : dataSnapshotCommerces.getChildren()) {
                        final Commerce commerce = snapshotCommerce.getValue(Commerce.class);
                        if (utilisateur.estInteresse(commerce) || (utilisateur.getIdCommerces() != null && utilisateur.estAbonne(snapshotCommerce.getKey()))) {
                            commerce.setId(snapshotCommerce.getKey());
                            referenceInterets.child(commerce.getIdInteret() + "").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshotInterets) {
                                    Interet interet = dataSnapshotInterets.getValue(Interet.class);
                                    if (interet != null) {
                                        commerce.setInteret(interet);
                                        referenceVilles.child(commerce.getIdVille() + "").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshotVilles) {
                                                Ville ville = dataSnapshotVilles.getValue(Ville.class);
                                                if (ville != null) {
                                                    commerce.setVille(ville);
                                                }
                                                if (utilisateur.estAbonne(snapshotCommerce.getKey())) {
                                                    commerce.abonner();
                                                }
                                                commerceAdapter.add(commerce);
                                                commerceAdapter.notifyDataSetChanged();
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }

    public static CommerceAdapter getCommerceAdapter() {
        return commerceAdapter;
    }

    public static void requestCommerceAbonner(final Commerce commerce) {
        utilisateur.getIdCommerces().add(commerce.getId());
        referenceUtilisateurs.child(utilisateur.getId()+"").child("idCommerces").setValue(utilisateur.getIdCommerces());
        refreshOffresAbonner(commerce);
    }
    public static void requestCommerceDesabonner(final Commerce commerce) {
        utilisateur.getIdCommerces().remove(commerce.getId());
        referenceUtilisateurs.child(utilisateur.getId()+"").child("idCommerces").setValue(utilisateur.getIdCommerces());
        refreshOffresDesabonner(commerce);
    }

    /******************************************** OFFRES ********************************************/
    public static void requestOffresUtilisateur() {
        if (offreAdapter.getCount() == 0 && offresUtilisateur.size() == 0) {
             referenceOffres.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final Offre offre = snapshot.getValue(Offre.class);
                        if (utilisateur.estAbonne(offre.getIdCommerce())) {
                            referenceCommerces.child(offre.getIdCommerce() + "").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Commerce commerce = dataSnapshot.getValue(Commerce.class);
                                    if (commerce != null) {
                                        offre.setCommerce(commerce);
                                        offreAdapter.add(offre);
                                        offreAdapter.notifyDataSetChanged();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }
    public static void refreshOffresAbonner(final Commerce commerce) {
        referenceOffres.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Offre offre = snapshot.getValue(Offre.class);
                    if (offre.getIdCommerce().equals(commerce.getId())) {
                        offre.setCommerce(commerce);
                        offreAdapter.add(offre);
                        offreAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    public static void refreshOffresDesabonner(final Commerce commerce) {
        referenceOffres.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Offre offre = snapshot.getValue(Offre.class);
                    if (offre.getIdCommerce().equals(commerce.getId())) {
                        offre.setCommerce(commerce);
                        offreAdapter.removeOffre(offre);
                        offreAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public static OffreAdapter getOffreAdapter() {
        return offreAdapter;
    }

    /******************************************** GROUPES ********************************************/
    public static void requestGroupesUtilisateur() {
        if (groupeUtilisateurAdapter.getCount() == 0 && groupesUtilisateur.size() == 0) {
            referenceGroupes.orderByChild("nom").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final Groupe groupe = snapshot.getValue(Groupe.class);
                        groupe.setId(snapshot.getKey());
                        if (utilisateur.aRejoint(groupe.getId())) {
                            referenceInterets.child(groupe.getIdInteret() + "").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Interet interet = dataSnapshot.getValue(Interet.class);
                                    if (interet != null) {
                                        groupe.setInteret(interet);
                                        groupe.rejoindre();
                                        groupeUtilisateurAdapter.add(groupe);
                                        groupeUtilisateurAdapter.notifyDataSetChanged();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                            referenceVilles.child(groupe.getIdVille() + "").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Ville ville = dataSnapshot.getValue(Ville.class);
                                    if (ville != null) {
                                        groupe.setVille(ville);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }
    public static void requestGroupesInteret() {
        if (groupeInteretsAdapter.getCount() == 0 && groupesInterets.size() == 0) {
            referenceGroupes.orderByChild("nom").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final Groupe groupe = snapshot.getValue(Groupe.class);
                        groupe.setId(snapshot.getKey());
                        if (utilisateur.estInteresse(groupe) && !(utilisateur.aRejoint(groupe.getId()))) {
                            referenceInterets.child(groupe.getIdInteret() + "").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Interet interet = dataSnapshot.getValue(Interet.class);
                                    if (interet != null) {
                                        groupe.setInteret(interet);
                                        groupeInteretsAdapter.add(groupe);
                                        groupeInteretsAdapter.notifyDataSetChanged();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                            referenceVilles.child(groupe.getIdVille() + "").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Ville ville = dataSnapshot.getValue(Ville.class);
                                    if (ville != null) {
                                        groupe.setVille(ville);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }

    public static void requestGroupeRejoindre(final Groupe groupe) {
        utilisateur.getIdGroupes().add(groupe.getId());
        referenceUtilisateurs.child(utilisateur.getId()+"").child("idGroupes").setValue(utilisateur.getIdGroupes());
        groupeInteretsAdapter.remove(groupe);
        groupeUtilisateurAdapter.add(groupe);
    }
    public static void requestGroupeQuitter(final Groupe groupe) {
        utilisateur.getIdGroupes().remove(groupe.getId());
        referenceUtilisateurs.child(utilisateur.getId()+"").child("idGroupes").setValue(utilisateur.getIdGroupes());
        groupeUtilisateurAdapter.remove(groupe);
        if (utilisateur.estInteresse(groupe)) {
            groupeInteretsAdapter.add(groupe);
        }
    }

    public static GroupeAdapter getGroupeUtilisateurAdapter(){
        return groupeUtilisateurAdapter;
    }
    public static GroupeAdapter getGroupeInteretAdapter(){
        return groupeInteretsAdapter;
    }

    public void setReferences(FirebaseDatabase database) {
        referenceActualites = database.getReference("Actualites");
        referenceCommerces = database.getReference("Commerces");
        referenceGroupes = database.getReference("Groupes");
        referenceInterets = database.getReference("Interets");
        referenceMessages = database.getReference("Messages");
        referenceOffres = database.getReference("Offres");
        referenceUtilisateurs = database.getReference("Utilisateurs");
        referenceVilles = database.getReference("Villes");
    }

    public void setAdapters() {
        actualitesUtilisateur = new ArrayList<Actualite>();
        actualiteAdapter = new ActualiteAdapter(this, actualitesUtilisateur);

        commercesUtilisateur = new ArrayList<Commerce>();
        commerceAdapter = new CommerceAdapter(this, commercesUtilisateur);

        offresUtilisateur = new ArrayList<Offre>();
        offreAdapter = new OffreAdapter(this, offresUtilisateur);

        groupesUtilisateur = new ArrayList<Groupe>();
        groupeUtilisateurAdapter = new GroupeAdapter(this, groupesUtilisateur);

        groupesInterets = new ArrayList<Groupe>();
        groupeInteretsAdapter = new GroupeAdapter(this, groupesInterets);
    }

    public static void refresh() {
        Log.d("REFRESH", "oui");

        actualitesUtilisateur.clear();
        requestActualitesUtilisateur();
        actualiteAdapter.notifyDataSetChanged();

        commercesUtilisateur.clear();
        requestCommercesUtilisateur();
        commerceAdapter.notifyDataSetChanged();

        groupesInterets.clear();
        requestGroupesInteret();
        groupeInteretsAdapter.notifyDataSetChanged();
    }
}
