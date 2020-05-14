package com.colman.fit_me.ui.recipes;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colman.fit_me.LoginActivity;
import com.colman.fit_me.MainActivity;
import com.colman.fit_me.R;
import com.colman.fit_me.RecyclerViewClickInterface;
import com.colman.fit_me.adapters.MyRecipesListAdapter;
import com.colman.fit_me.adapters.RecipeListAdapter;
import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.viewmodel.RecipeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyRecipesFragment extends Fragment implements RecyclerViewClickInterface {

    FirebaseAuth mFirebaseAuth;

    NavController nav;
    FloatingActionButton fab;
    private RecyclerView recyclerView;
    private MyRecipesListAdapter adapter;
    private RecipeViewModel mRecipeViewModel;
    View root;
    List<Recipe> recipesList =new ArrayList<>();
    private String pressed_category;

    public MyRecipesFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String email = LoginActivity.mFirebaseUser.getEmail();

        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_my_recipes, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        adapter = new MyRecipesListAdapter(recipesList, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        mRecipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        mRecipeViewModel.getRecipes().observe(this.getViewLifecycleOwner(), recipes -> {
            recipes.removeIf(recipe ->
                    (recipe.createdBy == null || (!recipe.createdBy.equals(email)))
            );
            recipesList = recipes;
            adapter.setRecipes(recipes);
        });

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("My Recipes");

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Floating action button
        nav = NavHostFragment.findNavController(this);

    }

    @Override
    public void onItemClick(int position) {

        MyRecipesFragmentDirections.ActionMyRecipesFragmentToEditRecipeFragment action = MyRecipesFragmentDirections.actionMyRecipesFragmentToEditRecipeFragment(recipesList.get(position));
        nav.navigate(action);

    }

    @Override
    public void onLongItemClick(int position) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //((MainActivity) getActivity()).getSupportActionBar().setTitle("Categories");
    }
}
