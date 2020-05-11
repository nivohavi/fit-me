package com.colman.fit_me.ui.recipes;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colman.fit_me.LoginActivity;
import com.colman.fit_me.MainActivity;
import com.colman.fit_me.R;
import com.colman.fit_me.RecyclerViewClickInterface;
import com.colman.fit_me.adapters.RecipeListAdapter;
import com.colman.fit_me.firebase.DatabaseHelper;
import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.utils.RQChangedListener;
import com.colman.fit_me.utils.RequestCode;
import com.colman.fit_me.viewmodel.RecipeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RecipeListFragment extends Fragment implements RecyclerViewClickInterface {

    Fragment currentFrag;
    NavController nav;
    public static final int NEW_RECIPE_ACTIVITY_REQUEST_CODE = 1;
    FloatingActionButton fab;
    ArrayList<Recipe> recipesList =new ArrayList<>();
    DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private RecipeListAdapter adapter;
    private RecipeViewModel mRecipeViewModel;
    View root;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentFrag =this;
        root = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        nav = NavHostFragment.findNavController(currentFrag);
        recyclerView = root.findViewById(R.id.recycler_view);
        final RecipeListAdapter adapter = new RecipeListAdapter(getContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        mRecipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
/*        mRecipeViewModel.getAllRecipes().observe(this.getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable final List<Recipe> recipes) {
                // Update the cached copy of the words in the adapter.
                adapter.setRecipes(recipes);
            }
        });*/

        mRecipeViewModel.getRecipes().observe(this.getViewLifecycleOwner(),recipes -> {
            Log.d("Niv", recipes.toString());
            adapter.setRecipes(recipes);
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        // Floating action button
        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeListFragmentDirections.ActionNavigationRecipeListToNavigationNewRecipe action = RecipeListFragmentDirections.actionNavigationRecipeListToNavigationNewRecipe("Categorie Name");
                action.setMessage("Categorie Name");
                nav.navigate(action);
            }
        });


        //Recipe added_recipe_name = RecipeListFragmentArgs.fromBundle(getArguments()).getRecipeObj();
        //mRecipeViewModel.insert(added_recipe_name);

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == NEW_RECIPE_ACTIVITY_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            Recipe recipe = new Recipe("1",data.getStringExtra(NewRecipeFragment.EXTRA_REPLY),"https://sdsadsa","Italian","This is description","This is directions", "This is ingredientsJson",new Date());
            mRecipeViewModel.insert(recipe);
        } else {
            Toast.makeText(
                    getContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLongItemClick(int position) {

    }



}
