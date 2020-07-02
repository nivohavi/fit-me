package com.colman.fit_me.ui.categories;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.colman.fit_me.MainActivity;
import com.colman.fit_me.R;
import com.colman.fit_me.RecyclerViewClickInterface;
import com.colman.fit_me.adapters.CategorieAdapter;
import com.colman.fit_me.model.Category;
import java.util.ArrayList;



public class CategoriesFragment extends Fragment implements RecyclerViewClickInterface {

    View root;
    NavController nav;
    ArrayList<Category> categoriesList =new ArrayList<>();
    private RecyclerView recyclerView;
    private CategorieAdapter adapter;
    private ImageView img_recipe;



    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        nav = NavHostFragment.findNavController(this);
        MainActivity.mainProgressBar.setVisibility(view.INVISIBLE);

    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.mainProgressBar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Categories");
        categoriesList = new ArrayList<Category>() {
            {
                add(new Category("Italian"));
                add(new Category("Israeli"));
                add(new Category("Asian"));
                add(new Category("Mexican"));
                add(new Category("French"));
                add(new Category("Category 6"));
            }
        };

        root = inflater.inflate(R.layout.fragment_categories, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        adapter = new CategorieAdapter(categoriesList, this);
        img_recipe = root.findViewById(R.id.imageView);


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

        CategoriesFragmentDirections.ActionNavigationCategoriesToNavigationRecipeList action = CategoriesFragmentDirections.actionNavigationCategoriesToNavigationRecipeList(categoriesList.get(position).name);
        action.setCategory(categoriesList.get(position).name);
        nav.navigate(action);
    }

    @Override
    public void onLongItemClick(int position) {

    }

    public void onBackPressed()
    {
        // Disable back button

    }

}
