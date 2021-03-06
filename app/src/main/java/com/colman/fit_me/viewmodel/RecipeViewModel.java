package com.colman.fit_me.viewmodel;

import android.app.Application;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.colman.fit_me.firebase.FirestoreManager;
import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.model.RecipeModel;
import com.colman.fit_me.sql.RecipeRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

class Constants {

    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "uploads";
}

public class RecipeViewModel extends AndroidViewModel {

    private final String REFERENCE_URL = "gs://matkon-5d8d7.appspot.com";
    private StorageReference storageRef;
    private UploadTask uploadTask;
    LiveData<List<Recipe>> liveData;



    private FirebaseFirestore fb;
    private FirebaseStorage fbStorage;
    private RecipeRepository mRepository;
    private LiveData<List<Recipe>> mAllRecipes;
    private MutableLiveData<List<Recipe>> recipes;
    private FirestoreManager firestoreManager;


    public RecipeViewModel (Application application)
    {
        super(application);
        fb = FirebaseFirestore.getInstance();
        mRepository = new RecipeRepository(application);
        mAllRecipes = mRepository.getAllRecipes();
        firestoreManager = FirestoreManager.newInstance();
        fbStorage = FirebaseStorage.getInstance();
        storageRef = fbStorage.getReferenceFromUrl(REFERENCE_URL);
    }


    ///////////////////// Eliav ////////////////////////

    public LiveData<List<Recipe>> getData() {
        if (liveData == null) {
            liveData = RecipeModel.instance.getAllRecipes();
        }
        return liveData;
    }

    public void refresh(RecipeModel.CompListener listener) {
        RecipeModel.instance.refreshRecipesList(listener);
    }


    ///////////////////////////////////////////////////






    public RecipeViewModel (){
        super(null);
    }

    public void update(Map<String, Object> data,MyCallback callback)
    {
        String recipeId = data.get("id").toString();
        mRepository.delete(recipeId);
        RecipeRepository.lud = new Date();
        fb.collection("recipes").document(recipeId).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                callback.onDataGot("Success");
            }
        });
    }


    public String getFirebaseId(){
        return fb.collection("recipes").document().getId();
    }


    public void insert(Recipe recipe, MyCallback callback)
    {
        //
        // Update Recipe is only to FireBase - not to Room(SQL)
        //
        fb.collection("recipes").document(recipe.getId()).set(recipe).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Niv", "XXXXXXXXXXXXXX -----  Success ----------- XXXXXXXXX");
                RecipeRepository.lud = new Date();
                callback.onDataGot("Inserted");
            }
        });
    }

    public void delete(String id,MyCallback callback)
    {

        fb.collection("recipes").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mRepository.deleteAllRecipes();
                callback.onDataGot("Deleted");
            }
        });
    }


    public void uploadImage(String id, Uri imagePath , MyCallback callback)
    {
        storageRef = fbStorage.getReference();
        StorageReference recipeImgRef = storageRef.child("images/"+imagePath.getLastPathSegment());
        uploadTask = recipeImgRef.putFile(imagePath);


        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                recipeImgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        callback.onDataGot(uri.toString());
                    }
                });
            }
        });
    }


    public LiveData<List<Recipe>> getRecipes() {
        if (recipes == null) {
            recipes = new MutableLiveData<List<Recipe>>();
            loadRecipes();
        }
        //return recipes;
        return mAllRecipes;
    }


    private void loadRecipes() {

        // 1. Get from local DB the last update DateTime of specific (each Category is row from local DB)
        // 2. Get all recipes By Category from Firebase newer than 'lud' timestamp
        // 3. If data != null
        // 3.1 push all recipes to SQL
        // 3.2 lud = newestRecipe.timestamp
        // 3.3 update SQL with new timestamp
        // 4. getAllRecipes() from SQL
        // 5. return getAllRecipes()

        // Do an asynchronous operation to fetch users.
/*        firestoreManager.getAllRecipesFirebase(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    List<Recipe> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        Recipe r = new Recipe(document.getData());
                        if(r.getTimestamp().after(d))
                        {
                            // Insert to SQL (Room)
                            mRepository.insert(r);
                        }
                        list.add(r);
                    }
                    recipes.setValue(list);
                }
                else
                {
                    Log.d("Niv", "Error getting documents: ", task.getException());
                }
            }
        });*/

        Date d = RecipeRepository.lud;


        firestoreManager.getAllRecipesFirebase(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null)
                {
                    Log.w("Niv", "Listen failed.", e);
                    return;
                }
                List<Recipe> list = new ArrayList<>();
                for (DocumentChange dc : value.getDocumentChanges())
                {
                    Recipe r = new Recipe(dc.getDocument().getData());
                    switch (dc.getType())
                    {
                        case ADDED:
                            mRepository.insert(r);
                            list.add(r);
                            break;
                        case MODIFIED:
                            mRepository.update(r);
                            list.forEach(recipe -> {
                                if(recipe.getId() == r.getId())
                                {
                                    recipe.setName(r.getName());
                                    recipe.setDescription(r.getDescription());
                                    recipe.setDirections(r.getDirections());
                                    recipe.setImgURL(r.getImgURL());
                                    recipe.setIngredientsJson(r.getIngredientsJson());
                                }
                            });
                            break;
                        case REMOVED:
                            mRepository.delete(r);
                            break;
                    }
                }

/*                for (QueryDocumentSnapshot document : value)
                {

                    Recipe r = new Recipe(document.getData());
                    //mRepository.recipeExists(r.getId());
                    if(r.getTimestamp().after(d))
                    {
                        // Insert to SQL (Room)
                        //mRepository.insert(r);
                        mRepository.insert(r);
                    }
                    list.add(r);
                }*/
                recipes.setValue(list);
            }
        });
    }

    public interface MyCallback {

        void onDataGot(String string);
    }


}
