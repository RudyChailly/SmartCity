package com.example.smartcity.Fragments.Chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.smartcity.Fragments.ParametresBottomSheetDialog;
import com.example.smartcity.R;
import com.example.smartcity.Notifications.Token;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private ChatViewModel chatViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chat, container, false);
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
        ((TabLayout)root.findViewById(R.id.chat_tabs)).setupWithViewPager(viewPager);

        updateToken(FirebaseInstanceId.getInstance().getToken());

        return root;
    }

    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter( getChildFragmentManager());

        adapter.addFragment(new ChatGroupesFragment(), getResources().getString(R.string.mes_groupes).toUpperCase());
        adapter.addFragment(new ChatRechercheFragment(), getResources().getString(R.string.rechercher).toUpperCase());
        viewPager.setAdapter(adapter);
        Log.d("SETVIEWPAGER", "OUI");
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

    public void updateToken(String refreshToken) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(refreshToken);
        reference.child(firebaseUser.getUid()).setValue(token);
    }
}