package com.example.smartcity.ui.chat.groupes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.smartcity.MainActivity;
import com.example.smartcity.R;
import com.example.smartcity.models.Interet.Interet;
import com.example.smartcity.models.Utilisateur;
import com.example.smartcity.models.groupe.Groupe;
import com.example.smartcity.ui.demarrage.Interets;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreationGroupe extends AppCompatActivity {

    DatabaseReference referenceInterets, referenceGroupes, referenceUtilisateurs;
    Interet interetSelectionne;

    private Utilisateur utilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_groupe);

        final ArrayList<Interet> liste_interets = new ArrayList<>();
        final ArrayAdapter<Interet> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, liste_interets);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinner_interets = findViewById(R.id.creation_groupe_interet);
        spinner_interets.setAdapter(adapter);
        spinner_interets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                interetSelectionne = adapter.getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        referenceInterets = database.getReference("Interets");
        referenceGroupes = database.getReference("Groupes");
        referenceUtilisateurs = database.getReference("Utilisateurs");
        referenceInterets.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Interet interet = snapshot.getValue(Interet.class);
                    interet.setId(snapshot.getKey());
                    if (MainActivity.getUtilisateur().getIdInterets().contains(interet.getId())) {
                        liste_interets.add(interet);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        findViewById(R.id.creation_groupe_valider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Vérifier le champ centre d'interet
                valider();
            }
        });

    }

    public void valider() {
            String nomValue = ((EditText)findViewById(R.id.creation_groupe_nom)).getText().toString();
            String descriptionValue =  ((EditText)findViewById(R.id.creation_groupe_description)).getText().toString();

            if (nomValue.length() > 15) {
                showDialogError(getResources().getString(R.string.message_nom_groupe_trop_long));
            }
            else if (nomValue.length() < 3) {
                showDialogError(getResources().getString(R.string.message_nom_groupe_trop_court));
            }
            else if (descriptionValue.length() > 20) {
                showDialogError(getResources().getString(R.string.message_description_trop_long));
            }
            else if (interetSelectionne == null) {
                showDialogError(getResources().getString(R.string.message_select_interet));
            }
            else {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("idInteret", interetSelectionne.getId());
                hashMap.put("idVille", "1");
                hashMap.put("nom", nomValue);
                hashMap.put("description", descriptionValue);

                final String key = referenceGroupes.push().getKey();

                referenceGroupes.child(key).setValue(hashMap);
                referenceGroupes.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshotGroupe) {
                        final Groupe groupe = dataSnapshotGroupe.getValue(Groupe.class);
                        referenceInterets.child(groupe.getIdInteret()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshotInteret) {
                                Interet interet = dataSnapshotInteret.getValue(Interet.class);
                                groupe.setInteret(interet);
                                groupe.rejoindre();
                                MainActivity.requestGroupeRejoindre(groupe);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
    }

    public void showDialogError(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle(message);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        AlertDialog alert = dialog.create();
        alert.show();
    }



}
