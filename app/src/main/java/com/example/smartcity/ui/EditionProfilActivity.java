package com.example.smartcity.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.smartcity.MainActivity;
import com.example.smartcity.R;
import com.example.smartcity.models.Utilisateur;
import com.example.smartcity.ui.chat.groupes.CreationGroupe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.internal.Util;

public class EditionProfilActivity extends AppCompatActivity {

    TextView edition_email;
    EditText edition_prenom, edition_nom;
    CircleImageView edition_image;
    Button edition_valider;

    Utilisateur utilisateur;

    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
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
                    edition(utilisateur, prenomValue, nomValue);
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

    private void edition(final Utilisateur utilisateur, final String prenom, final String nom) {
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
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(this, "Upload en cours", Toast.LENGTH_SHORT).show();
            }
            else {
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
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(utilisateur.getId() + "." + getFileExtension(imageUri));
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(EditionProfilActivity.this, "Image chargée", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditionProfilActivity.this, "ERREUR", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }
    }
}
