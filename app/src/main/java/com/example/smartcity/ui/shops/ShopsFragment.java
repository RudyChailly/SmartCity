package com.example.smartcity.ui.shops;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.smartcity.MainActivity;
import com.example.smartcity.R;

public class ShopsFragment extends Fragment {

    private ShopsViewModel shopsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shopsViewModel =
                ViewModelProviders.of(this).get(ShopsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_shops, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.Shops).toUpperCase());
        final TextView textView = root.findViewById(R.id.text_dashboard);
        shopsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}