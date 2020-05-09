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
import com.example.smartcity.models.Utilisateur;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
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

    Utilisateur utilisateur;

    DatabaseReference
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

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        RequestQueue queue = Volley.newRequestQueue(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        setReferences(database);

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
        //requestUtilisateur();

    }

    /******************************************** UTILISATEURS ********************************************/
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public DatabaseReference getReferenceUtilisateurs() {
        return referenceUtilisateurs;
    }

    /******************************************** ACTUALITES ********************************************/
    public void requestActualitesUtilisateur() {
        if (actualiteAdapter.getCount() == 0) {
            referenceActualites.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final Actualite actualite = snapshot.getValue(Actualite.class);
                        if (utilisateur.estInteresse(actualite)) {
                            referenceInterets.child(actualite.getIdInteret() + "").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Interet interet = dataSnapshot.getValue(Interet.class);
                                    if (interet != null) {
                                        actualite.setInteret(interet);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                            actualiteAdapter.add(actualite);
                            actualiteAdapter.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }

    public ActualiteAdapter getActualiteAdapter() {
        return actualiteAdapter;
    }

    /******************************************** COMMERCES ********************************************/
    public void requestCommercesUtilisateur() {
        if (commerceAdapter.getCount() == 0) {
            referenceCommerces.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final Commerce commerce = snapshot.getValue(Commerce.class);
                        if (utilisateur.estInteresse(commerce) || (utilisateur.getIdCommerces() != null && utilisateur.estAbonne(Integer.parseInt(snapshot.getKey())))) {
                            commerce.setId(Integer.parseInt(snapshot.getKey()));
                            referenceInterets.child(commerce.getIdInteret() + "").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Interet interet = dataSnapshot.getValue(Interet.class);
                                    if (interet != null) {
                                        commerce.setInteret(interet);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                            referenceVilles.child(commerce.getIdVille() + "").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Ville ville = dataSnapshot.getValue(Ville.class);
                                    if (ville != null) {
                                        commerce.setVille(ville);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                            if (utilisateur.estAbonne(Integer.parseInt(snapshot.getKey()))) {
                                commerce.abonner();
                            }
                            commerceAdapter.add(commerce);
                            commerceAdapter.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }

    public CommerceAdapter getCommerceAdapter() {
        return commerceAdapter;
    }

    public void requestCommerceAbonner(final Commerce commerce) {
        utilisateur.getIdCommerces().add(commerce.getId());
        referenceUtilisateurs.child(utilisateur.getId()+"").child("idCommerces").setValue(utilisateur.getIdCommerces());
        refreshOffresAbonner(commerce);
    }
    public void requestCommerceDesabonner(final Commerce commerce) {
        utilisateur.getIdCommerces().remove(commerce.getId());
        referenceUtilisateurs.child(utilisateur.getId()+"").child("idCommerces").setValue(utilisateur.getIdCommerces());
        refreshOffresDesabonner(commerce);
    }

    /******************************************** OFFRES ********************************************/
    public void requestOffresUtilisateur() {
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
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                            offreAdapter.add(offre);
                            offreAdapter.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }
    public void refreshOffresAbonner(final Commerce commerce) {
        referenceOffres.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Offre offre = snapshot.getValue(Offre.class);
                    if (offre.getIdCommerce() == commerce.getId()) {
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
    public void refreshOffresDesabonner(final Commerce commerce) {
        referenceOffres.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Offre offre = snapshot.getValue(Offre.class);
                    if (offre.getIdCommerce() == commerce.getId()) {
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

    public OffreAdapter getOffreAdapter() {
        return offreAdapter;
    }

    /******************************************** GROUPES ********************************************/
    public void requestGroupesUtilisateur() {
        if (groupeUtilisateurAdapter.getCount() == 0) {
            referenceGroupes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final Groupe groupe = snapshot.getValue(Groupe.class);
                        groupe.setId(Integer.parseInt(snapshot.getKey()));
                        if (utilisateur.aRejoint(groupe.getId())) {
                            referenceInterets.child(groupe.getIdInteret() + "").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Interet interet = dataSnapshot.getValue(Interet.class);
                                    if (interet != null) {
                                        groupe.setInteret(interet);
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
                            groupe.rejoindre();
                            groupeUtilisateurAdapter.add(groupe);
                            groupeUtilisateurAdapter.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }
    public void requestGroupesInteret() {
        if (groupeInteretsAdapter.getCount() == 0) {
            referenceGroupes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final Groupe groupe = snapshot.getValue(Groupe.class);
                        groupe.setId(Integer.parseInt(snapshot.getKey()));
                        if (utilisateur.estInteresse(groupe) && !(utilisateur.aRejoint(groupe.getId()))) {
                            referenceInterets.child(groupe.getIdInteret() + "").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Interet interet = dataSnapshot.getValue(Interet.class);
                                    if (interet != null) {
                                        groupe.setInteret(interet);
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
                            groupeInteretsAdapter.add(groupe);
                            groupeInteretsAdapter.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }

    public void requestGroupeRejoindre(final Groupe groupe) {
        utilisateur.getIdGroupes().add(groupe.getId());
        referenceUtilisateurs.child(utilisateur.getId()+"").child("idGroupes").setValue(utilisateur.getIdGroupes());
        groupeInteretsAdapter.remove(groupe);
        groupeUtilisateurAdapter.add(groupe);
    }
    public void requestGroupeQuitter(final Groupe groupe) {
        utilisateur.getIdGroupes().remove(groupe.getId());
        referenceUtilisateurs.child(utilisateur.getId()+"").child("idGroupes").setValue(utilisateur.getIdGroupes());
        groupeUtilisateurAdapter.remove(groupe);
        if (utilisateur.estInteresse(groupe)) {
            groupeInteretsAdapter.add(groupe);
        }
    }

    public GroupeAdapter getGroupeUtilisateurAdapter(){
        return groupeUtilisateurAdapter;
    }
    public GroupeAdapter getGroupeInteretAdapter(){
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


}
