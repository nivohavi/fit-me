package com.colman.fit_me.sql;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.colman.fit_me.model.Recipe;
import com.google.firebase.firestore.auth.User;

import java.util.List;

@Dao
public interface RecipeDao {
    @Query("SELECT * FROM recipe_table")
    List<Recipe> getAll();

    @Query("SELECT * FROM recipe_table WHERE id IN (:userIds)")
    List<Recipe> loadAllByIds(int[] userIds);

/*    @Query("SELECT * FROM recipe_table WHERE name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Recipe recipe);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Recipe... recipes);

    @Update
    void update(Recipe recipe);

    @Query("UPDATE recipe_table SET isDeleted=:setIsDeleted WHERE id = :id")
    void updateIsDeleted(Boolean setIsDeleted, String id);


    @Delete
    void delete(Recipe recipe);

    @Query("DELETE FROM recipe_table WHERE id = (:id)")
    void delete(String id);

    @Query("DELETE FROM recipe_table")
    void deleteAll();

    @Query("SELECT * from recipe_table ORDER BY name ASC")
    LiveData<List<Recipe>> getAlphabetizedRecipes();

    @Query("SELECT * FROM recipe_table")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM recipe_table WHERE createdBy = (:email)")
    LiveData<List<Recipe>> getAllRecipesByUser(String email);


}