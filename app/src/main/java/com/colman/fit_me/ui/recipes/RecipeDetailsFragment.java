package com.colman.fit_me.ui.recipes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.colman.fit_me.MainActivity;
import com.colman.fit_me.R;
import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.ui.categories.CategoriesFragment;
import com.colman.fit_me.viewmodel.RecipeViewModel;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Map;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailsFragment extends Fragment {

    View root;
    NavController nav;
    private Recipe r;
    private RecipeViewModel mRecipeViewModel;
    private TextView tv_recipe_name;
    private TextView tv_recipe_description;
    private TextView tv_recipe_ing;
    private TextView tv_recipe_directions;

    private ImageView img_recipe;





    public RecipeDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        mRecipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        r = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeObj();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(r.getName());
        MainActivity.mainProgressBar.setVisibility(View.VISIBLE);


        return root;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        nav = NavHostFragment.findNavController(this);
        tv_recipe_name = view.findViewById(R.id.txt_recipe_name);
        tv_recipe_description = view.findViewById(R.id.txt_recipe_description);
        tv_recipe_ing = view.findViewById(R.id.txt_recipe_ing);
        tv_recipe_directions = view.findViewById(R.id.txt_recipe_directions);
        img_recipe = view.findViewById(R.id.img_recipe);


        tv_recipe_name.setText(r.getName());
        tv_recipe_description.setText(r.getDescription());
        tv_recipe_ing.setText(r.getIngredientsJson());
        tv_recipe_directions.setText(r.getDirections());

        if(!r.getImgURL().equals(""))
        {
            Picasso.get().load(r.getImgURL()).into(img_recipe, new com.squareup.picasso.Callback(){

                @Override
                public void onSuccess() {
                    MainActivity.mainProgressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError(Exception e) {
                    if(isAdded())
                    {
                        img_recipe.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));
                        MainActivity.mainProgressBar.setVisibility(View.INVISIBLE);

                    }

                }
            });
        }
        else {
            Picasso.get().load(R.drawable.ic_launcher_foreground).into(img_recipe);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(r.getCategory());
    }


}
