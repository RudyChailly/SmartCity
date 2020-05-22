package com.example.smartcity.ui.chat.messages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcity.MainActivity;
import com.example.smartcity.R;
import com.example.smartcity.models.Utilisateur;
import com.example.smartcity.models.groupe.message.Message;
import com.example.smartcity.models.groupe.message.MessageAdapter;
import com.example.smartcity.notifications.APIService;
import com.example.smartcity.notifications.Client;
import com.example.smartcity.notifications.Data;
import com.example.smartcity.notifications.MyResponse;
import com.example.smartcity.notifications.Sender;
import com.example.smartcity.notifications.Token;
import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    String idGroupe, nomGroupe;
    EditText messageChamps;
    ImageButton messageBouton;

    DatabaseReference referenceUtilisateurs, referenceMessages;

    MessageAdapter messageAdapter;
    ArrayList<Message> messages;

    RecyclerView recyclerView;
    Intent intent;

    APIService apiService;

    HashMap<String, Utilisateur> utilisateursNomPrenom;

    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView = findViewById(R.id.liste_messages);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent = getIntent();
        idGroupe = intent.getStringExtra("idGroupe");
        nomGroupe = intent.getStringExtra("nomGroupe");
        ((TextView) findViewById(R.id.toolbar_title)).setText(nomGroupe);

        messageChamps = findViewById(R.id.message_champs);
        messageBouton = findViewById(R.id.message_bouton);

        messageBouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String contenu = messageChamps.getText().toString();
                if (!(contenu.equals(""))) {
                    envoyerMessage(contenu);
                }
                else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message.", Toast.LENGTH_SHORT).show();
                }
                messageChamps.setText("");
            }
        });

        referenceUtilisateurs = FirebaseDatabase.getInstance().getReference("Utilisateurs");
        referenceMessages = FirebaseDatabase.getInstance().getReference("Messages");
        utilisateursNomPrenom  = new HashMap<>();
        referenceUtilisateurs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Utilisateur utilisateur = snapshot.getValue(Utilisateur.class);
                    if (utilisateur.aRejoint(idGroupe)) {
                        Log.d("Utilisateur", utilisateur.toString());
                        utilisateursNomPrenom.put(utilisateur.getId(), utilisateur);
                    }
                }
                lireMessages(idGroupe);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void envoyerMessage(final String contenu) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Message message = new Message(firebaseUser.getUid(), idGroupe, contenu);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("idUtilisateur",message.getIdUtilisateur());
        hashMap.put("idGroupe", message.getIdGroupe());
        hashMap.put("contenu", message.getContenu());
        hashMap.put("date", Calendar.getInstance().getTime());
        referenceMessages.push().setValue(hashMap);

        final String msg = contenu;
        Utilisateur utilisateurCourant;
        referenceUtilisateurs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Utilisateur utilisateur = snapshot.getValue(Utilisateur.class);
                    Log.d("UTIL", utilisateur.toString());
                    /*if (utilisateur.getId().equals(firebaseUser.getUid())) {
                        int a = 2;
                    } else {*/
                        if (utilisateur.getIdGroupes().contains(idGroupe)) {
                            sendNotification(utilisateur.getId(), contenu);
                            notify = false;
                        /*}*/
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(final String receiver, final String message) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Utilisateur utilisateur = MainActivity.getUtilisateur();
                    Data data = new Data(utilisateur, firebaseUser.getUid(), message, nomGroupe, receiver, idGroupe, nomGroupe);
                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                        }
                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void lireMessages(final String idGroupe) {
        messages = new ArrayList<>();
        referenceMessages.orderByChild("idGroupe").equalTo(idGroupe).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Message message = snapshot.getValue(Message.class);
                    if (message.getIdGroupe() != null && message.getIdGroupe().equals(idGroupe)) {
                        messages.add(message);
                        messageAdapter = new MessageAdapter(MessageActivity.this, messages);
                        recyclerView.setAdapter(messageAdapter);
                        Log.d("NP", utilisateursNomPrenom.size()+"");
                        message.setUtilisateur(utilisateursNomPrenom.get(message.getIdUtilisateur()));
                        /*referenceUtilisateurs.child(message.getIdUtilisateur()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Utilisateur utilisateur = dataSnapshot.getValue(Utilisateur.class);
                                message.setUtilisateur(utilisateur);
                                messages.add(message);
                                messageAdapter = new MessageAdapter(MessageActivity.this, messages);
                                recyclerView.setAdapter(messageAdapter);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                        });*/
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
