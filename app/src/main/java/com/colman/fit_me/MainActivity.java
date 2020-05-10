package com.colman.fit_me;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.colman.fit_me.ui.categories.CategoriesFragment;
import com.colman.fit_me.ui.recipes.NewRecipeFragment;
import com.colman.fit_me.ui.recipes.RecipeListFragment;
import com.colman.fit_me.viewmodel.RecipeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity{

    AppBarConfiguration appBarConfiguration;
    private RecipeViewModel mWordViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.



        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_categories, R.id.navigation_recipe_list, R.id.navigation_user_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return navController.navigateUp();
        //return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

/*
    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }
*/

    @Override
    public void onBackPressed()
    {
        // Disable back button
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
    }



}
