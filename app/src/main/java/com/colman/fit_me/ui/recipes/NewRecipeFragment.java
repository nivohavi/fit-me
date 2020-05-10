package com.colman.fit_me.ui.recipes;

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

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.colman.fit_me.R;
import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.utils.RQChangedListener;
import com.colman.fit_me.utils.RequestCode;
import com.colman.fit_me.viewmodel.RecipeViewModel;

import java.util.Date;
import java.util.NavigableMap;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewRecipeFragment extends Fragment {

    View root;
    NavController nav;
    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";
    private EditText mEditRecipeView;
    public static String RQ_Result;
    public static String recipeName;
    public RQChangedListener rq;
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

        TextView tv = view.findViewById(R.id.textViewAmount);
        //String categorie = NewRecipeFragmentArgs.fromBundle(getArguments()).getMessage();
        //tv.setText(categorie + "");


        nav = NavHostFragment.findNavController(this);
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
                    Random rand =new Random();
                    Integer x = new Integer(rand.nextInt());
                    String recipe_name = mEditRecipeView.getText().toString();
                    Recipe r = new Recipe(x.toString(),recipe_name,"https://sdsadsa","Italian","This is description","This is directions", "This is ingredientsJson",new Date());

                    // New Recipe Insertion
                    mRecipeViewModel.insert(r);
                    NewRecipeFragmentDirections.ActionNavigationNewRecipeToNavigationRecipeList action = NewRecipeFragmentDirections.actionNavigationNewRecipeToNavigationRecipeList();
                    nav.navigate(action);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){}




}
