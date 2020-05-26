package com.example.smartcity.Fragments.Demarrage;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smartcity.Activities.MainActivity;
import com.example.smartcity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ConnexionFragment extends Fragment {

    EditText connexion_email, connexion_password;
    Button connexion_valider;
    String email_value, password_value;

    FirebaseAuth auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (connexion_email != null) {
            savedInstanceState.putString("email_value", connexion_email.getText().toString());
        }
        if (connexion_password != null) {
            savedInstanceState.putString("password_value", connexion_password.getText().toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demarrage_connexion, container, false);
        connexion_email = view.findViewById(R.id.connexion_email);
        connexion_password = view.findViewById(R.id.connexion_password);
        connexion_valider = view.findViewById(R.id.connexion_valider);

        auth = FirebaseAuth.getInstance();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("email_value")) {
                email_value = savedInstanceState.getString("email_value");
                connexion_email.setText(email_value);
            }
            if (savedInstanceState.containsKey("password_value")) {
                password_value = savedInstanceState.getString("password_value");
                connexion_password.setText(email_value);
            }
        }

        connexion_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_value = connexion_email.getText().toString();
                password_value = connexion_password.getText().toString();

                if (TextUtils.isEmpty(email_value)) {
                    showDialogError(getResources().getString(R.string.email_requis));
                }
                else if (TextUtils.isEmpty(password_value)) {
                    showDialogError(getResources().getString(R.string.mot_de_passe_requis));
                }
                else {
                    auth.signInWithEmailAndPassword(email_value, password_value).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                getActivity().finish();
                            }
                            else {
                                showDialogError(getResources().getString(R.string.connexion_echec));
                            }
                        }
                    });
                }
            }
        });
        return view;
    }

    public void showDialogError(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
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



