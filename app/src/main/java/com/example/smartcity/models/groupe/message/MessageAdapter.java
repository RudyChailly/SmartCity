package com.example.smartcity.models.groupe.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartcity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_RECEIVED = 0;
    public static final int MSG_TYPE_SEND = 1;

    private Context context;
    private List<Message> messages;

    FirebaseUser utilisateur;

    public MessageAdapter(Context context, List<Message> messages) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.message_send, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.message_received, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.contenu.setText(message.getContenu());
        if (holder.utilisateur != null && message.getUtilisateur() != null) {
            holder.utilisateur.setText(message.getUtilisateur().toString());
        }
        if (holder.image != null && message.getUtilisateur().getImageURL() != null) {
            Glide.with(context).load(message.getUtilisateur().getImageURL()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        utilisateur = FirebaseAuth.getInstance().getCurrentUser();
        if (messages.get(position).getIdUtilisateur().equals(utilisateur.getUid())) {
            return MSG_TYPE_SEND;
        }
        else {
            return MSG_TYPE_RECEIVED;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView contenu;
        public TextView utilisateur;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            contenu = itemView.findViewById(R.id.message_contenu);
            utilisateur = itemView.findViewById(R.id.message_utilisateur);
            image = itemView.findViewById(R.id.message_utilisateur_image);
        }
    }

}
