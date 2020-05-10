package com.colman.fit_me.ui.categories;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.ArrayList;



public class CategoriesFragment extends Fragment implements RecyclerViewClickInterface {

    public static final int NEW_RECIPE_ACTIVITY_REQUEST_CODE = 1;
    ArrayList<Category> categoriesList =new ArrayList<>();
    private RecyclerView recyclerView;
    private CategorieAdapter adapter;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        categoriesList.add(new Category("Category 1"));
        categoriesList.add(new Category("Category 2"));
        categoriesList.add(new Category("Category 3"));
        categoriesList.add(new Category("Category 4"));
        categoriesList.add(new Category("Category 5"));
        categoriesList.add(new Category("Category 6"));

        View root = inflater.inflate(R.layout.fragment_categories, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        adapter = new CategorieAdapter(categoriesList, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }


    @Override
    public void onItemClick(int position) {
        Toast.makeText(getActivity() , categoriesList.get(position).name, Toast.LENGTH_SHORT).show();
        //Intent i = new Intent(getActivity(), ExActivity.class);
        //startActivity(i);
        NavController nav = NavHostFragment.findNavController(this);
        //nav.popBackStack(R.id.navigation_exercises, false);
        nav.navigate(R.id.action_navigation_categories_to_navigation_exercises);



    }

    @Override
    public void onLongItemClick(int position) {

    }

    public void onBackPressed()
    {
        // Disable back button

    }
}
