package com.colman.fit_me.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.colman.fit_me.R;
import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.ui.categories.CategoriesFragment;
import com.colman.fit_me.viewmodel.RecipeViewModel;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailsFragment extends Fragment {

    View root;
    NavController nav;
    private EditText mEditRecipeView;
    private Recipe r;
    private RecipeViewModel mRecipeViewModel;



    public RecipeDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        mRecipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        return root;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        nav = NavHostFragment.findNavController(this);
        TextView tv_recipe_name = view.findViewById(R.id.txt_recipe_name);
        TextView tv_recipe_description = view.findViewById(R.id.txt_recipe_description);
        TextView tv_recipe_ing = view.findViewById(R.id.txt_recipe_ing);
        TextView tv_recipe_directions = view.findViewById(R.id.txt_recipe_directions);



        r = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeObj();
        tv_recipe_name.setText(r.getName());
        tv_recipe_description.setText(r.getDescription());
        tv_recipe_ing.setText(r.getIngredientsJson());
        tv_recipe_directions.setText(r.getDirections());


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){}




}
