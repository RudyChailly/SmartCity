package com.example.smartcity.ui.actualites.meteo;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.smartcity.MainActivity;
import com.example.smartcity.R;

public class ActualitesMeteoFragment extends Fragment {

    private LocationManager locationManager;
    TextView actualite_meteo_lat_value;
    TextView actualite_meteo_long_value;
    Button actualite_meteo_bouton;

    public ActualitesMeteoFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actualites_meteo, container, false);


        actualite_meteo_lat_value = view.findViewById(R.id.actualite_meteo_lat_value);
        actualite_meteo_long_value = view.findViewById(R.id.actualite_meteo_long_value);
        actualite_meteo_bouton = view.findViewById(R.id.actualite_meteo_bouton);
        ((MainActivity)getActivity()).requestLocation(actualite_meteo_lat_value, actualite_meteo_long_value);
        return view;
    }



}