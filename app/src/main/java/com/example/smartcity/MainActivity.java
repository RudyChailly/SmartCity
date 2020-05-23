package com.example.smartcity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcity.models.Utilisateur;
import com.example.smartcity.models.actualite.ActualiteAdapter;
import com.example.smartcity.models.commerce.CommerceAdapter;
import com.example.smartcity.models.commerce.offre.OffreAdapter;
import com.example.smartcity.models.groupe.Groupe;
import com.example.smartcity.models.Interet.Interet;
import com.example.smartcity.models.actualite.Actualite;
import com.example.smartcity.models.commerce.Commerce;
import com.example.smartcity.models.commerce.offre.Offre;
import com.example.smartcity.models.Ville;
import com.example.smartcity.models.groupe.GroupeAdapter;
import com.example.smartcity.ui.demarrage.Demarrage;
import com.example.smartcity.ui.demarrage.Interets;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static ArrayList<Actualite> actualitesUtilisateur;
    private static ArrayList<Commerce> commercesUtilisateur;
    private static ArrayList<Offre> offresUtilisateur;

    private static ArrayList<Groupe> groupesUtilisateur, groupesInterets;

    private static ActualiteAdapter actualiteAdapter;
    private static OffreAdapter offreAdapter;
    private static CommerceAdapter commerceAdapter;
    private static GroupeAdapter groupeUtilisateurAdapter, groupeInteretsAdapter;

    private static Utilisateur utilisateur;

    private LocationManager locationManager;
    private double latitude, longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    Location currentLocation;
    TextView latitude_text, longitude_text;
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
            // Verifie que l'utilisateur a au moins un centre d'interet
            requestUtilisateur(firebaseUser);
        }
        else {
            Intent intent = new Intent(MainActivity.this, Demarrage.class);
            startActivity(intent);
            finish();
        }
    }

    /******************************************** METEO ********************************************/
    public void requestLocation(TextView latitude_text, TextView longitude_text) {
        this.latitude_text = latitude_text;
        this.longitude_text = longitude_text;
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }
        else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void getLocation() {
        /*locationRequest = new LocationRequest();
        locationRequest.setInterval(120000); // two minute interval
        locationRequest.setFastestInterval(120000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        Intent locationIntent = new Intent(getApplicationContext(), MainActivity.class);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, PendingIntent.getService(this, 0, locationIntent, PendingIntent.FLAG_UPDATE_CURRENT));*/
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    latitude_text.setText(location.getLatitude()+"");
                    longitude_text.setText(location.getLongitude()+"");
                }
            }
        });
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
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
                    Intent intent = new Intent(MainActivity.this, Interets.class);
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
        if (actualiteAdapter.getCount() == 0) {
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
        if (commerceAdapter.getCount() == 0) {
            referenceCommerces.addListenerForSingleValueEvent(new ValueEventListener() {
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
        if (offreAdapter.getCount() == 0) {
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
        if (groupeUtilisateurAdapter.getCount() == 0) {
            referenceGroupes.addListenerForSingleValueEvent(new ValueEventListener() {
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
        if (groupeInteretsAdapter.getCount() == 0) {
            referenceGroupes.addListenerForSingleValueEvent(new ValueEventListener() {
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
