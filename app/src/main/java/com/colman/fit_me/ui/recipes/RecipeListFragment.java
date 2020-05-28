package com.colman.fit_me.ui.recipes;



import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colman.fit_me.MainActivity;
import com.colman.fit_me.R;
import com.colman.fit_me.RecyclerViewClickInterface;
import com.colman.fit_me.adapters.RecipeListAdapter;

import com.colman.fit_me.model.Recipe;

import com.colman.fit_me.viewmodel.RecipeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RecipeListFragment extends Fragment implements RecyclerViewClickInterface {

    NavController nav;
    FloatingActionButton fab;
    private RecyclerView recyclerView;
    private TextView tv_no_data;
    private RecipeListAdapter adapter;
    private RecipeViewModel mRecipeViewModel;
    View root;
    List<Recipe> recipesList =new ArrayList<>();
    private String pressed_category;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        tv_no_data = root.findViewById(R.id.tv_no_data);

        adapter = new RecipeListAdapter(recipesList, this);

         pressed_category = RecipeListFragmentArgs.fromBundle(getArguments()).getCategory();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        mRecipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        mRecipeViewModel.getRecipes().observe(this.getViewLifecycleOwner(), recipes -> {
            recipes.removeIf(recipe -> (!recipe.getCategory().equals(pressed_category)));
            if(!recipes.isEmpty()){
                tv_no_data.setVisibility(View.INVISIBLE);
                recipesList = recipes;
                //adapter.notifyDataSetChanged();
            }
            else{
                tv_no_data.setVisibility(View.VISIBLE);
            }
            adapter.setRecipes(recipes);
            MainActivity.mainProgressBar.setVisibility(View.INVISIBLE);

        });

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(pressed_category);


        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MainActivity.mainProgressBar.setVisibility(view.VISIBLE);

        // Floating action button
        nav = NavHostFragment.findNavController(this);
        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeListFragmentDirections.ActionNavigationRecipeListToNavigationNewRecipe action = RecipeListFragmentDirections.actionNavigationRecipeListToNavigationNewRecipe(pressed_category);
                action.setMessage(pressed_category);
                nav.navigate(action);

            }
        });
    }


    @Override
    public void onItemClick(int position) {

        Recipe r =null;
        RecipeListFragmentDirections.ActionNavigationRecipeListToNavigationRecipeDetails action = RecipeListFragmentDirections.actionNavigationRecipeListToNavigationRecipeDetails(recipesList.get(position));
        action.setRecipeObj(recipesList.get(position));
        nav.navigate(action);

    }

    @Override
    public void onLongItemClick(int position) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
