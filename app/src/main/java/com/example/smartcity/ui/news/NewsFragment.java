package com.example.smartcity.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.smartcity.MainActivity;
import com.example.smartcity.R;

public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        //Toolbar toolbar = (Toolbar)getResources().getLayout(R.layout.toolbar);

        //Toolbar toolbar = ((Toolbar)root.findViewById(R.id.toolbar));
        //((MainActivity)getActivity()).setSupportActionBar(toolbar);

        //((Toolbar) getView().findViewById(R.id.toolbar)).setTitle("NEWS");
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.News).toUpperCase());
        final TextView textView = root.findViewById(R.id.text_home);

        newsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}