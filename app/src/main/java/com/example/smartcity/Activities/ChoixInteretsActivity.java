package com.example.smartcity.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.example.smartcity.R;
import com.example.smartcity.Models.Interet;
import com.example.smartcity.Adapters.InteretAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChoixInteretsActivity extends AppCompatActivity {

    ArrayList interets;
    ArrayList idInteretsSelectionnes;

    InteretAdapter interetAdapter;
    GridView gridView_interets;
    DatabaseReference referenceInterets;

    public ChoixInteretsActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_interets);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        referenceInterets = database.getReference("Interets");
        interets = new ArrayList<Interet>();
        idInteretsSelectionnes = new ArrayList<String>();

        if (getIntent() != null && getIntent().hasExtra("idInteretsSelectionnes")) {
            idInteretsSelectionnes = getIntent().getStringArrayListExtra("idInteretsSelectionnes");
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("interets")) {
                interets = savedInstanceState.getParcelableArrayList("interets");
            }
            if (savedInstanceState.containsKey("idInteretsSelectionnes")) {
                idInteretsSelectionnes = savedInstanceState.getParcelableArrayList("idInteretsSelectionnes");
            }
        }

        interetAdapter = new InteretAdapter(ChoixInteretsActivity.this, interets);
        gridView_interets = findViewById(R.id.liste_interets);
        gridView_interets.setAdapter(interetAdapter);

        requestInterets();

        findViewById(R.id.interets_valider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valider();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("interets", interets);
        savedInstanceState.putParcelableArrayList("idInteretsSelectionnes", idInteretsSelectionnes);
    }

    public void requestInterets() {
        if (interetAdapter.getCount() == 0) {
            referenceInterets.orderByChild("nom").addListenerForSingleValueEvent(new ValueEventListener() {
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
            if (getIntent() != null && getIntent().hasExtra("redirectToMainActivity") && getIntent().getBooleanExtra("redirectToMainActivity", false) == true) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else {
                MainActivity.refresh();
            }
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
