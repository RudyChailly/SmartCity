package com.example.smartcity.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smartcity.Activities.MainActivity;
import com.example.smartcity.Activities.EditionProfilActivity;
import com.example.smartcity.Activities.DemarrageActivity;
import com.example.smartcity.Activities.ChoixInteretsActivity;
import com.example.smartcity.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

public class ParametresBottomSheetDialog extends BottomSheetDialogFragment {

    private BottomSheetListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_parametres, container, false);
        TextView parametres_bouton_profile = view.findViewById(R.id.parametres_bouton_profil);
        parametres_bouton_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profilIntent = new Intent(getContext(), EditionProfilActivity.class);
                startActivity(profilIntent);
                dismiss();
            }
        });

        TextView parametres_bouton_interets = view.findViewById(R.id.parametres_bouton_interets);
        parametres_bouton_interets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent interetsIntent = new Intent(getContext(), ChoixInteretsActivity.class);
                interetsIntent.putExtra("idInteretsSelectionnes", MainActivity.getUtilisateur().getIdInterets());
                startActivity(interetsIntent);
                dismiss();
            }
        });
        TextView parametres_bouton_deconnexion = view.findViewById(R.id.parametres_bouton_deconnexion);
        parametres_bouton_deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent demarrageIntent = new Intent(getContext(), DemarrageActivity.class);
                startActivity(demarrageIntent);
                getActivity().finish();
                dismiss();
            }
        });
        return view;
    }

    public interface BottomSheetListener {
        void onButtonCLicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        }
        catch(ClassCastException e) {}

    }

}
