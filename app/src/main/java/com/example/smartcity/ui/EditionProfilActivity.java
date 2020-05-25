package com.example.smartcity.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.smartcity.MainActivity;
import com.example.smartcity.R;
import com.example.smartcity.models.Utilisateur;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditionProfilActivity extends AppCompatActivity {

    TextView edition_email;
    EditText edition_prenom, edition_nom;
    String email_value, prenom_value, nom_value;
    CircleImageView edition_image;
    Button edition_valider;

    Utilisateur utilisateur;

    StorageReference storageReference;
    static final int IMAGE_REQUEST = 1;
    StorageTask uploadTask;
    Uri imageUri;
    String uploadImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edition_profil);

        edition_image = findViewById(R.id.edition_image);
        edition_email = findViewById(R.id.edition_email);
        edition_prenom = findViewById(R.id.edition_prenom);
        edition_nom = findViewById(R.id.edition_nom);
        edition_valider = findViewById(R.id.edition_valider);

        utilisateur = MainActivity.getUtilisateur();
        edition_email.setText(utilisateur.getEmail());
        edition_prenom.setText(utilisateur.getPrenom());
        edition_nom.setText(utilisateur.getNom());

        if (utilisateur.getImageURL() != null) {
            Glide.with(this).load(utilisateur.getImageURL()).into(edition_image);
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("email_value")) {
                email_value = savedInstanceState.getString("email_value");
                edition_email.setText(email_value);
            }
            if (savedInstanceState.containsKey("nom_value")) {
                nom_value = savedInstanceState.getString("nom_value");
                edition_nom.setText(nom_value);
            }
            if (savedInstanceState.containsKey("prenom_value")) {
                prenom_value = savedInstanceState.getString("prenom_value");
                edition_prenom.setText(prenom_value);
            }
            if (savedInstanceState.containsKey("imageUri")) {
                imageUri = savedInstanceState.getParcelable("imageUri");
                edition_image.setImageURI(imageUri);
            }
            if (savedInstanceState.containsKey("uploadImageUri")) {
                uploadImageUri = savedInstanceState.getString("uploadImageUri");
            }
        }

        edition_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prenomValue = edition_prenom.getText().toString();
                String nomValue = edition_nom.getText().toString();
                if (TextUtils.isEmpty(nomValue)) {
                    showDialogError(getResources().getString(R.string.nom_requis));
                }
                else if (TextUtils.isEmpty(prenomValue)) {
                    showDialogError(getResources().getString(R.string.prenom_requis));
                }
                else {
                    valider(utilisateur, prenomValue, nomValue);
                }
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference("Utilisateurs");
        edition_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (edition_email != null) {
            savedInstanceState.putString("email_value", edition_email.getText().toString());
        }
        if (edition_prenom != null) {
            savedInstanceState.putString("prenom_value", edition_prenom.getText().toString());
        }
        if (edition_nom != null) {
            savedInstanceState.putString("nom_value", edition_nom.getText().toString());
        }
        if (imageUri != null) {
            savedInstanceState.putParcelable("imageUri", imageUri);
        }
        if (uploadImageUri != null) {
            savedInstanceState.putString("uploadImageUri", uploadImageUri);
        }
    }

    private void valider(final Utilisateur utilisateur, final String prenom, final String nom) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("prenom", prenom);
        values.put("nom", nom);
        if (imageUri != null) {
            values.put("imageURL", uploadImageUri);
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Utilisateurs").child(utilisateur.getId());
        reference.updateChildren(values).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    utilisateur.setPrenom(prenom);
                    utilisateur.setNom(nom);
                    if (imageUri != null) {
                        utilisateur.setImageURL(uploadImageUri);
                    }
                    finish();
                }
            }
        });
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

    private void imageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            edition_image.setImageURI(imageUri);
            if (!(uploadTask != null && uploadTask.isInProgress())) {
                uploadImage();
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(R.string.Chargement));
        pd.show();

        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(utilisateur.getId() + "." + getFileExtension(imageUri));
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            uploadImageUri = uri.toString();
                        }
                    });
                    pd.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                }
            });
        }
    }
}
