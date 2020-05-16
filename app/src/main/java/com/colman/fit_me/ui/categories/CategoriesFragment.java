package com.colman.fit_me.ui.categories;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colman.fit_me.ExActivity;
import com.colman.fit_me.LoginActivity;
import com.colman.fit_me.MainActivity;
import com.colman.fit_me.R;
import com.colman.fit_me.RecyclerViewClickInterface;
import com.colman.fit_me.adapters.CategorieAdapter;
import com.colman.fit_me.model.Category;
import com.colman.fit_me.ui.recipes.RecipeListFragmentDirections;

import java.util.ArrayList;



public class CategoriesFragment extends Fragment implements RecyclerViewClickInterface {

    View root;
    NavController nav;
    ArrayList<Category> categoriesList =new ArrayList<>();
    private RecyclerView recyclerView;
    private CategorieAdapter adapter;
    public static String categoryPressed;
    private ImageView img_recipe;



    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        nav = NavHostFragment.findNavController(this);
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Categories");
        categoriesList.add(new Category("Italian"));
        categoriesList.add(new Category("Israeli"));
        categoriesList.add(new Category("Asian"));
        categoriesList.add(new Category("Mexican"));
        categoriesList.add(new Category("French"));
        categoriesList.add(new Category("Category 6"));

        root = inflater.inflate(R.layout.fragment_categories, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        adapter = new CategorieAdapter(categoriesList, this);
        img_recipe = root.findViewById(R.id.imageView);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //getActivity().setTitle("Categories");
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }


    @Override
    public void onItemClick(int position) {
        Toast.makeText(getActivity() , categoriesList.get(position).name, Toast.LENGTH_SHORT).show();

        CategoriesFragmentDirections.ActionNavigationCategoriesToNavigationRecipeList action = CategoriesFragmentDirections.actionNavigationCategoriesToNavigationRecipeList(categoriesList.get(position).name);
        action.setCategory(categoriesList.get(position).name);
        nav.navigate(action);

    }

    @Override
    public void onLongItemClick(int position) {

    }

    public void onBackPressed()
    {
        // Disable back button

    }
}
