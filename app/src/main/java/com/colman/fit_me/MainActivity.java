package com.colman.fit_me;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.colman.fit_me.ui.categories.CategoriesFragment;
import com.colman.fit_me.ui.recipes.NewRecipeFragment;
import com.colman.fit_me.ui.recipes.RecipeListFragment;
import com.colman.fit_me.ui.user_profile.UserProfileViewModel;
import com.colman.fit_me.viewmodel.NewRecipeViewModel;
import com.colman.fit_me.viewmodel.RecipeViewModel;
import com.colman.fit_me.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity{

    AppBarConfiguration appBarConfiguration;
    public static ProgressBar mainProgressBar;
    public static Context context;
    private UserProfileViewModel userProfileViewModel;



    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);

        userProfileViewModel.isUserLoggedIn(data -> {
            if(!data)
            {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mainProgressBar = findViewById(R.id.progressBar);
        context = getApplicationContext();


        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_categories,R.id.navigation_my_recipes, R.id.navigation_user_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }


/*    @Override
    public void onBackPressed()
    {
        // Disable back button
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
    }





}
