package com.example.smartcity.ui.demarrage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.example.smartcity.MainActivity;
import com.example.smartcity.R;
import com.example.smartcity.models.Interet.Interet;
import com.example.smartcity.models.Interet.InteretAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Interets extends AppCompatActivity {

    private ArrayList<Interet> interets;
    private ArrayList<String> idInteretsSelectionnes;
    private InteretAdapter interetAdapter;
    private GridView gridView_interets;
    private DatabaseReference referenceUtilisateur, referenceInterets;

    public Interets() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interets);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        referenceInterets = database.getReference("Interets");
        interets = new ArrayList<>();
        interetAdapter = new InteretAdapter(Interets.this, interets);
        requestInterets();
        gridView_interets = findViewById(R.id.liste_interets);
        gridView_interets.setAdapter(interetAdapter);
        idInteretsSelectionnes = new ArrayList<>();

        findViewById(R.id.bouton_interets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valider();
            }
        });
    }

    public void requestInterets() {
        if (interetAdapter.getCount() == 0) {
            referenceInterets.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final Interet interet = snapshot.getValue(Interet.class);
                        interet.setId(snapshot.getKey());
                        interetAdapter.add(interet);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }

    public boolean estSelectionne(String id) {
        return idInteretsSelectionnes.contains(id);
    }

    public void selectInteret(String id) {
        if (!idInteretsSelectionnes.contains(id)) {
            idInteretsSelectionnes.add(id);
        }
    }

    public void deselectInteret(String id) {
        if (idInteretsSelectionnes.contains(id)) {
            idInteretsSelectionnes.remove(id);
        }
    }

    public void valider() {
        if (idInteretsSelectionnes.size() == 0) {
            showDialogError(getResources().getString(R.string.choix_centres_interet_erreur));
        }
        else {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference("Utilisateurs").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("idInterets").setValue(idInteretsSelectionnes);
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    public void showDialogError(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle(message);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alert = dialog.create();
        alert.show();
    }
}
