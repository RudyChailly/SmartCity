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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.smartcity.MainActivity;
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