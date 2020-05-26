package com.example.smartcity.ui.actualites;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.smartcity.ParametresBottomSheetDialog;
import com.example.smartcity.R;
import com.example.smartcity.ui.actualites.agenda.ActualitesAgendaFragment;
import com.example.smartcity.ui.actualites.alarmes.ActualitesAlarmesFragment;
import com.example.smartcity.ui.actualites.meteo.ActualitesMeteoFragment;
import com.example.smartcity.ui.actualites.news.ActualitesNewsFragment;
import com.example.smartcity.ui.commerces.CommercesFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ActualitesFragment extends Fragment {

    private ActualitesViewModel actualitesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        actualitesViewModel = ViewModelProviders.of(this).get(ActualitesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_actualites, container, false);
        ViewPager viewPager = (ViewPager)root.findViewById(R.id.view_pager);
        setViewPager(viewPager);
        ImageButton bouton_parametres = root.findViewById(R.id.bouton_parametres);
        bouton_parametres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParametresBottomSheetDialog modal_parametres = new ParametresBottomSheetDialog();
                modal_parametres.show(getFragmentManager(), "modalParametres");
            }
        });

        ((TabLayout)root.findViewById(R.id.actualites_tabs)).setupWithViewPager(viewPager);
        return root;
    }

    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter( getChildFragmentManager());

        adapter.addFragment(new ActualitesMeteoFragment(), getResources().getString(R.string.météo).toUpperCase());
        adapter.addFragment(new ActualitesNewsFragment(), getResources().getString(R.string.news).toUpperCase());
        //adapter.addFragment(new ActualitesAlarmesFragment(), getResources().getString(R.string.alarmes).toUpperCase());
        //adapter.addFragment(new ActualitesAgendaFragment(), getResources().getString(R.string.agenda).toUpperCase());
        viewPager.setAdapter(adapter);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}