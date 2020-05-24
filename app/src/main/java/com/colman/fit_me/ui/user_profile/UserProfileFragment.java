package com.colman.fit_me.ui.user_profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavOptions;

import com.colman.fit_me.LoginActivity;
import com.colman.fit_me.MainActivity;
import com.colman.fit_me.R;
import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.ui.home.HomeViewModel;
import com.colman.fit_me.ui.recipes.NewRecipeFragmentDirections;
import com.colman.fit_me.viewmodel.RecipeViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Date;

public class UserProfileFragment extends Fragment {

    private UserProfileViewModel userProfileViewModel;
    Button btnLogout;
    private TextView txt_user_email;
    private final int PICK_IMAGE_REQUEST = 71;
    private ProgressBar progressBar;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static FirebaseUser mFirebaseUser;
    private ImageView img_user;
    private Uri filePath;
    View root;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("My Profile");

        userProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        mFirebaseAuth = FirebaseAuth.getInstance();



        // Get the specific view
        root = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser == null ) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        };

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        txt_user_email = (TextView)view.findViewById(R.id.txt_user_email);
        txt_user_email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        img_user = (ImageView)view.findViewById(R.id.profile_image);
        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        // Logout button
        btnLogout = (Button) view.findViewById(R.id.logoutButton);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(getActivity(), LoginActivity.class);
                startActivity(intToMain);


            }

        });
    }



    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // TODO: Fix when not picking picture
        if(requestCode == PICK_IMAGE_REQUEST
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            Picasso.get().load(filePath).into(img_user);
        }
    }


}
