package com.example.smartcity.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcity.R;
import com.example.smartcity.Models.Utilisateur;
import com.example.smartcity.Models.Message;
import com.example.smartcity.Adapters.MessageAdapter;
import com.example.smartcity.Notifications.APIService;
import com.example.smartcity.Notifications.Client;
import com.example.smartcity.Notifications.Data;
import com.example.smartcity.Notifications.MyResponse;
import com.example.smartcity.Notifications.Sender;
import com.example.smartcity.Notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    String idGroupe, nomGroupe;
    EditText message_contenu;
    ImageButton message_envoyer;

    DatabaseReference referenceUtilisateurs, referenceMessages;

    MessageAdapter messageAdapter;
    ArrayList messages;

    RecyclerView recyclerView;
    Intent intent;

    APIService apiService;

    HashMap<String, Utilisateur> utilisateurs;

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

        message_contenu = findViewById(R.id.message_contenu);
        message_envoyer = findViewById(R.id.message_envoyer);

        message_envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contenu = message_contenu.getText().toString();
                if (!(contenu.equals(""))) {
                    envoyerMessage(contenu);
                }
                else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message.", Toast.LENGTH_SHORT).show();
                }
                message_contenu.setText("");
            }
        });

        referenceUtilisateurs = FirebaseDatabase.getInstance().getReference("Utilisateurs");
        referenceMessages = FirebaseDatabase.getInstance().getReference("Messages");

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("message_value")) {
                message_contenu.setText(savedInstanceState.getString("message_value"));
            }
            if (savedInstanceState.containsKey("utilisateurs")) {
                utilisateurs = (HashMap<String, Utilisateur>) savedInstanceState.getSerializable("utilisateurs");
            }
            lireMessages();
        }
        if (utilisateurs == null){
            utilisateurs  = new HashMap<>();
            referenceUtilisateurs.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Utilisateur utilisateur = snapshot.getValue(Utilisateur.class);
                        if (utilisateur.aRejoint(idGroupe)) {
                            utilisateurs.put(utilisateur.getId(), utilisateur);
                        }
                    }
                    lireMessages();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("message_value", message_contenu.getText().toString());
        if (utilisateurs != null) {
            savedInstanceState.putSerializable("utilisateurs", utilisateurs);
        }
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
                    if (!(utilisateur.getId().equals(firebaseUser.getUid()))) {
                        if (utilisateur.getIdGroupes().contains(idGroupe)) {
                            sendNotification(utilisateur.getId(), contenu);
                        }
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

    private void lireMessages() {
        messages = new ArrayList<Message>();
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
                        message.setUtilisateur(utilisateurs.get(message.getIdUtilisateur()));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
