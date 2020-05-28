package com.colman.fit_me.ui.recipes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.colman.fit_me.MainActivity;
import com.colman.fit_me.R;
import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.viewmodel.RecipeViewModel;
import com.google.firebase.Timestamp;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditRecipeFragment extends Fragment {

    View root;
    NavController nav;
    private Recipe r;
    private Map<String, Object> data;
    private final int PICK_IMAGE_REQUEST = 71;
    private RecipeViewModel mRecipeViewModel;
    private EditText et_recipe_name,et_recipe_description,et_recipe_ing,et_recipe_directions;
    private ImageView img_recipe;
    private ProgressBar progressBar;
    private Uri filePath;
    int count = 0;
    boolean isPickedImage = false;



    public EditRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_edit_recipe, container, false);
        mRecipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        r = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeObj();
        data = new HashMap<>();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(r.getName());


        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        nav = NavHostFragment.findNavController(this);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        et_recipe_name = (EditText)view.findViewById(R.id.edit_recipe_name);
        et_recipe_description = (EditText)view.findViewById(R.id.edit_recipe_description);
        et_recipe_ing = (EditText)view.findViewById(R.id.edit_recipe_ing);
        et_recipe_directions = (EditText)view.findViewById(R.id.edit_recipe_directions);

        img_recipe = view.findViewById(R.id.img_recipe);
        img_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        et_recipe_name.setText(r.getName());
        et_recipe_description.setText(r.getDescription());
        et_recipe_ing.setText(r.getIngredientsJson());
        et_recipe_directions.setText(r.getDirections());

        if(!r.getImgURL().equals(""))
        {
            Picasso.get().load(r.getImgURL()).into(img_recipe, new com.squareup.picasso.Callback(){

                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    if(isAdded())
                    {
                        img_recipe.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));
                    }

                }
            });
        }
        else
        {
            Picasso.get().load(R.drawable.ic_launcher_foreground).into(img_recipe);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_edit_recipe, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete_button:
                mRecipeViewModel.delete(r.getId(), new RecipeViewModel.MyCallback() {
                    @Override
                    public void onDataGot(String string) {
                        nav.navigate(R.id.action_navigation_edit_recipe_to_navigation_my_recipes);
                    }
                });
                return true;
            case R.id.save_button:
                if (count == 0)
                {
                    count++;
                    progressBar.setVisibility(item.getActionView().VISIBLE);
                    data.put("id",r.getId());
                    data.put("name",et_recipe_name.getText().toString());
                    data.put("category",r.getCategory());
                    data.put("description",et_recipe_description.getText().toString());
                    data.put("directions",et_recipe_directions.getText().toString());
                    data.put("ingredientsJson",et_recipe_ing.getText().toString());
                    data.put("imgURL",r.getImgURL());
                    data.put("timestamp",new Timestamp(new Date()));
                    if(isPickedImage)
                    {
                        mRecipeViewModel.uploadImage(r.getId(), filePath, new RecipeViewModel.MyCallback() {
                            @Override
                            public void onDataGot(String string) {
                                data.put("imgURL",string);
                                // New Recipe Insertion - only to Firebase
                                mRecipeViewModel.update(data, new RecipeViewModel.MyCallback() {
                                    @Override
                                    public void onDataGot(String string) {
                                        nav.navigate(R.id.action_navigation_edit_recipe_to_navigation_my_recipes);
                                        progressBar.setVisibility(item.getActionView().INVISIBLE);
                                        count=0;
                                    }
                                });
                            }
                        });
                    }
                    else
                    {
                        mRecipeViewModel.update(data, new RecipeViewModel.MyCallback() {
                            @Override
                            public void onDataGot(String string) {
                                nav.navigate(R.id.action_navigation_edit_recipe_to_navigation_my_recipes);
                                progressBar.setVisibility(item.getActionView().INVISIBLE);
                                count=0;
                            }
                        });
                    }
                    isPickedImage = false;
                }
                else{
                    Toast.makeText(getActivity(),"Uploading...",Toast.LENGTH_SHORT).show();
                    break;
                }
                isPickedImage = false;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
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
            Picasso.get().load(filePath).into(img_recipe);
            isPickedImage = true;

        }
    }
}
