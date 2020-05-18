package com.colman.fit_me.ui.recipes;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.colman.fit_me.LoginActivity;
import com.colman.fit_me.R;
import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.viewmodel.RecipeViewModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewRecipeFragment extends Fragment {

    private static final int RESULT_OK = 1;
    View root;
    NavController nav;
    private EditText mEditRecipeView;
    private EditText et_recipe_name,et_recipe_description,et_recipe_ing,et_recipe_directions;
    private String recipe_name,recipe_description,recipe_ing,recipe_directions;
    private ImageView img_recipe;

    private Button btnChoose;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

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
        pressed_category = NewRecipeFragmentArgs.fromBundle(getArguments()).getMessage();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_new_recipe, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.submit_button:
                String newID = mRecipeViewModel.getFirebaseId();
                Recipe r = new Recipe(newID,et_recipe_name.getText().toString(),"placeholder", LoginActivity.mFirebaseUser.getEmail(),pressed_category,et_recipe_description.getText().toString(),et_recipe_directions.getText().toString(),et_recipe_ing.getText().toString(),new Date());
                if (!checkForm())
                {
                    Toast.makeText(getActivity(),"Not all fields were filled",Toast.LENGTH_SHORT).show();
                    break;
                }
                // Here will be image upload
                mRecipeViewModel.uploadImage(r.getId(), filePath, new RecipeViewModel.MyCallback() {
                    @Override
                    public void onDataGot(String string) {
                        // New Recipe Insertion - only to Firebase
                        r.setImgURL(string);
                        mRecipeViewModel.insert(r, new RecipeViewModel.MyCallback() {
                            @Override
                            public void onDataGot(String string) {
                                NewRecipeFragmentDirections.ActionNavigationNewRecipeToNavigationRecipeList action = NewRecipeFragmentDirections.actionNavigationNewRecipeToNavigationRecipeList(r.getCategory());
                                nav.navigate(action, new NavOptions.Builder().setPopUpTo(R.id.navigation_recipe_list,true).build());
                                //nav.popBackStack(R.id.navigation_recipe_list, true);

                            }
                        });

                    }
                });


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
        if(requestCode == PICK_IMAGE_REQUEST
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            Picasso.get().load(filePath).into(img_recipe);
        }
    }

    public boolean checkForm(){
        if (TextUtils.isEmpty(et_recipe_name.getText()) || TextUtils.isEmpty(et_recipe_description.getText()) || TextUtils.isEmpty(et_recipe_ing.getText()) || TextUtils.isEmpty(et_recipe_directions.getText()))
        {
            return false;
        }
        return true;
    }

}
