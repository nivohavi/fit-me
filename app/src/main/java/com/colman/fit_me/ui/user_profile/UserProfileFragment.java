package com.colman.fit_me.ui.user_profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.colman.fit_me.LoginActivity;
import com.colman.fit_me.R;
import com.colman.fit_me.ui.home.HomeViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileFragment extends Fragment {

    private UserProfileViewModel userProfileViewModel;
    Button btnLogout;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private HomeViewModel homeViewModel;
    View view;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userProfileViewModel =
                ViewModelProviders.of(this).get(UserProfileViewModel.class);

        // Get the specific view
        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);

        // Change the TextView
/*        final TextView textView = root.findViewById(R.id.text_dashboard);
        userProfileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        // Logout button
        btnLogout = (Button) root.findViewById(R.id.logoutButton);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(getActivity(), LoginActivity.class);
                startActivity(intToMain);
            }
        });



        return root;
    }
}
