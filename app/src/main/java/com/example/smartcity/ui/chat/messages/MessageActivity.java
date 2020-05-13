package com.example.smartcity.ui.chat.messages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcity.R;
import com.example.smartcity.models.Utilisateur;
import com.example.smartcity.models.groupe.message.Message;
import com.example.smartcity.models.groupe.message.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {

    Integer idGroupe;
    EditText messageChamps;
    ImageButton messageBouton;

    DatabaseReference referenceUtilisateurs, referenceMessages;

    MessageAdapter messageAdapter;
    ArrayList<Message> messages;

    RecyclerView recyclerView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        recyclerView = findViewById(R.id.liste_messages);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent = getIntent();
        idGroupe = intent.getIntExtra("groupe_id",0);
        ((TextView) findViewById(R.id.toolbar_title)).setText(intent.getStringExtra("groupe_nom"));

        messageChamps = findViewById(R.id.message_champs);
        messageBouton = findViewById(R.id.message_bouton);

        messageBouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        lireMessages(idGroupe);

    }

    private void envoyerMessage(String contenu) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Message message = new Message(firebaseUser.getUid(), idGroupe, contenu);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("idUtilisateur",message.getIdUtilisateur());
        hashMap.put("idGroupe", message.getIdGroupe());
        hashMap.put("contenu", message.getContenu());
        hashMap.put("date", Calendar.getInstance().getTime());
        referenceMessages.push().setValue(hashMap);
    }

    private void lireMessages(final Integer idGroupe) {
        messages = new ArrayList<>();
        referenceMessages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message.getIdGroupe().equals(idGroupe)) {
                        messages.add(message);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, messages);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
