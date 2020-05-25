package com.example.smartcity.ui.demarrage;

import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.smartcity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class InscriptionFragment extends Fragment {

    EditText inscription_email, inscription_password, inscription_confirm_password, inscription_nom, inscription_prenom;
    Button inscription_valider;
    String email_value, password_value, confirm_password_value, nom_value, prenom_value;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (inscription_email != null) {
            savedInstanceState.putString("email_value", inscription_email.getText().toString());
        }
        if (inscription_password != null) {
            savedInstanceState.putString("password_value", inscription_password.getText().toString());
        }
        if (inscription_confirm_password != null) {
            savedInstanceState.putString("confirm_password_value", inscription_confirm_password.getText().toString());
        }
        if (inscription_nom != null) {
            savedInstanceState.putString("nom_value", inscription_nom.getText().toString());
        }
        if (inscription_prenom != null) {
            savedInstanceState.putString("prenom_value", inscription_prenom.getText().toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demarrage_inscription, container, false);
        inscription_email = view.findViewById(R.id.inscription_email);
        inscription_password = view.findViewById(R.id.inscription_password);
        inscription_confirm_password = view.findViewById(R.id.inscription_confirm_password);
        inscription_prenom = view.findViewById(R.id.inscription_prenom);
        inscription_nom = view.findViewById(R.id.inscription_nom);
        inscription_valider = view.findViewById(R.id.inscription_valider);

        auth = FirebaseAuth.getInstance();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("email_value")) {
                email_value = savedInstanceState.getString("email_value");
                inscription_email.setText(email_value);
            }
            if (savedInstanceState.containsKey("password_value")) {
                password_value = savedInstanceState.getString("password_value");
                inscription_password.setText(email_value);
            }
            if (savedInstanceState.containsKey("confirm_password_value")) {
                confirm_password_value = savedInstanceState.getString("confirm_password_value");
                inscription_confirm_password.setText(confirm_password_value);
            }
            if (savedInstanceState.containsKey("nom_value")) {
                nom_value = savedInstanceState.getString("nom_value");
                inscription_nom.setText(nom_value);
            }
            if (savedInstanceState.containsKey("prenom_value")) {
                prenom_value = savedInstanceState.getString("prenom_value");
                inscription_prenom.setText(prenom_value);
            }
        }

        inscription_valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                email_value = inscription_email.getText().toString();
                password_value = inscription_password.getText().toString();
                confirm_password_value = inscription_confirm_password.getText().toString();
                prenom_value = inscription_prenom.getText().toString();
                nom_value = inscription_nom.getText().toString();

                if (TextUtils.isEmpty(nom_value)) {
                    showDialogError(getResources().getString(R.string.nom_requis));
                }
                else if (TextUtils.isEmpty(prenom_value)) {
                    showDialogError(getResources().getString(R.string.prenom_requis));
                }
                else if (TextUtils.isEmpty(email_value)) {
                    showDialogError(getResources().getString(R.string.email_requis));
                }
                else if (TextUtils.isEmpty(password_value)) {
                    showDialogError(getResources().getString(R.string.mot_de_passe_requis));
                }
                else if (password_value.length() < 8){
                    showDialogError(getResources().getString(R.string.mot_de_passe_trop_court));
                }
                else if (!(password_value.equals(confirm_password_value))){
                    showDialogError(getResources().getString(R.string.mot_de_passe_pas_identiques));
                }
                else {
                    inscription(email_value, password_value, prenom_value, nom_value);
                }
            }
        });
        return view;
    }

    private void inscription(final String email, String password, final String prenom, final String nom) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userid = firebaseUser.getUid();
                    reference = FirebaseDatabase.getInstance().getReference("Utilisateurs").child(userid);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("email", email);
                    hashMap.put("prenom", prenom);
                    hashMap.put("nom", nom);

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getContext(), ChoixInteretsActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("redirectToMainActivity", true);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }
                    });
                }
                else {
                    showDialogError(getResources().getString(R.string.compte_existant));
                }
            }
        });
    }

    public void showDialogError(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
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
