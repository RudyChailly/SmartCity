package com.example.smartcity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smartcity.models.Interet.Interet;
import com.example.smartcity.ui.demarrage.Demarrage;
import com.example.smartcity.ui.demarrage.Interets;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.security.spec.ECField;

public class ParametresBottomSheetDialog extends BottomSheetDialogFragment {

    private BottomSheetListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_parametres, container, false);
        TextView parametres_bouton_interets = view.findViewById(R.id.parametres_bouton_interets);
        parametres_bouton_interets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent interetsIntent = new Intent(getContext(), Interets.class);
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
                Intent demarrageIntent = new Intent(getContext(), Demarrage.class);
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
