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

import com.example.smartcity.MainActivity;
import com.example.smartcity.R;
import com.example.smartcity.models.Interet.Interet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class InscriptionFragment extends Fragment {

    private EditText email, password, confirm_password, nom, prenom;
    private Button bouton_inscription;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demarrage_inscription, container, false);
        email = view.findViewById(R.id.inscription_email);
        password = view.findViewById(R.id.inscription_password);
        confirm_password = view.findViewById(R.id.inscription_confirm_password);
        prenom = view.findViewById(R.id.inscription_prenom);
        nom = view.findViewById(R.id.inscription_nom);
        bouton_inscription = view.findViewById(R.id.inscription_envoyer);

        auth = FirebaseAuth.getInstance();

        bouton_inscription.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();
                String confirm_passwordValue = confirm_password.getText().toString();
                String prenomValue = prenom.getText().toString();
                String nomValue = nom.getText().toString();

                if (TextUtils.isEmpty(nomValue)) {
                    showDialogError(getResources().getString(R.string.nom_requis));
                }
                else if (TextUtils.isEmpty(prenomValue)) {
                    showDialogError(getResources().getString(R.string.prenom_requis));
                }
                else if (TextUtils.isEmpty(emailValue)) {
                    showDialogError(getResources().getString(R.string.email_requis));
                }
                else if (TextUtils.isEmpty(passwordValue)) {
                    showDialogError(getResources().getString(R.string.mot_de_passe_requis));
                }
                else if (passwordValue.length() < 8){
                    showDialogError(getResources().getString(R.string.mot_de_passe_trop_court));
                }
                else if (!(passwordValue.equals(confirm_passwordValue))){
                    showDialogError(getResources().getString(R.string.mot_de_passe_pas_identiques));
                }
                else {
                    inscription(emailValue, passwordValue, prenomValue, nomValue);
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

                                Intent intent = new Intent(getContext(), Interets.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
