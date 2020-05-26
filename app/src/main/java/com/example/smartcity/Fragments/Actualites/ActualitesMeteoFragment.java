package com.example.smartcity.Fragments.Actualites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.smartcity.Activities.MainActivity;
import com.example.smartcity.R;

public class ActualitesMeteoFragment extends Fragment {

    public ActualitesMeteoFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actualites_meteo, container, false);

        TextView view_meteo_ville = view.findViewById(R.id.meteo_ville);
        ImageView view_meteo_image = view.findViewById(R.id.meteo_image);
        TextView view_meteo_temperature = view.findViewById(R.id.meteo_temperature);
        TextView view_meteo_description = view.findViewById(R.id.meteo_description);
        ((MainActivity)getActivity()).requestLocation(view_meteo_ville, view_meteo_image, view_meteo_temperature, view_meteo_description);
        return view;
    }



}