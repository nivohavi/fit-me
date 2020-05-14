package com.colman.fit_me.ui.recipes;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.colman.fit_me.R;
import com.colman.fit_me.firebase.FirestoreManager;
import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.ui.categories.CategoriesFragment;
import com.colman.fit_me.utils.RQChangedListener;
import com.colman.fit_me.utils.RequestCode;
import com.colman.fit_me.viewmodel.RecipeViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewRecipeFragment extends Fragment {

    View root;
    NavController nav;
    private EditText mEditRecipeView;
    private String pressed_category;

    private RecipeViewModel mRecipeViewModel;



    public NewRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_new_recipe, container, false);
        mRecipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        return root;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        nav = NavHostFragment.findNavController(this);

        pressed_category = NewRecipeFragmentArgs.fromBundle(getArguments()).getMessage();
        //NavigationUI.setupWithNavController(R.id.nav_view, nav);


        mEditRecipeView = root.findViewById(R.id.edit_word);
        final Button button = root.findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(mEditRecipeView.getText()))
                {
                    Toast.makeText(getActivity(),"Not all fields were filled",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String recipe_name = mEditRecipeView.getText().toString();
                    UUID generatedId = UUID.randomUUID();
                    Recipe r = new Recipe(generatedId.toString(),recipe_name,"https://sdsadsa", pressed_category,"This is description","This is directions", "This is ingredientsJson",new Date());

                    // New Recipe Insertion - only to Firebase
                    mRecipeViewModel.insert(r);
                    NewRecipeFragmentDirections.ActionNavigationNewRecipeToNavigationRecipeList action = NewRecipeFragmentDirections.actionNavigationNewRecipeToNavigationRecipeList(r.getCategory());
                    nav.navigate(action);
                    //nav.navigate(R.id.action_navigation_new_recipe_to_navigation_recipe_list);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){}




}
