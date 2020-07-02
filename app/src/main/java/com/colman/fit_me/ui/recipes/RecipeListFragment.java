package com.colman.fit_me.ui.recipes;



import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.colman.fit_me.MainActivity;
import com.colman.fit_me.R;
import com.colman.fit_me.RecyclerViewClickInterface;
import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.model.RecipeModel;
import com.colman.fit_me.viewmodel.RecipeListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RecipeListFragment extends Fragment implements RecyclerViewClickInterface {

    public static NavController nav;
    FloatingActionButton fab;
    private TextView tv_no_data;
    private RecipeListViewModel viewModel;
    View root;
    public static List<Recipe> recipesList =new ArrayList<>();
    private String pressed_category;
    private ProgressBar progressBar;
    LiveData<List<Recipe>> liveData;
    RecyclerView list;
    public static List<Recipe> data = new LinkedList<Recipe>();
    NewRecipesListAdapter new_adapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        list = root.findViewById(R.id.recycler_view);
        list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);
        new_adapter = new NewRecipesListAdapter();
        list.setAdapter(new_adapter);

        progressBar = root.findViewById(R.id.progressBar);
        tv_no_data = root.findViewById(R.id.tv_no_data);
        pressed_category = RecipeListFragmentArgs.fromBundle(getArguments()).getCategory();

        liveData = viewModel.getData();
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if(!recipes.isEmpty())
                {
                    tv_no_data.setVisibility(View.INVISIBLE);
                    data = recipes;
                    data.removeIf(recipe -> (recipe.isDeleted() || !recipe.getCategory().equals(pressed_category)));
                    new_adapter.notifyDataSetChanged();
                }
                else
                {
                    tv_no_data.setVisibility(View.VISIBLE);
                }
                MainActivity.mainProgressBar.setVisibility(View.INVISIBLE);
            }

        });

        final SwipeRefreshLayout swipeRefresh = root.findViewById(R.id.recipes_list_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh(new RecipeModel.CompListener() {
                    @Override
                    public void onComplete() {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });


        ((MainActivity) getActivity()).getSupportActionBar().setTitle(pressed_category);


        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MainActivity.mainProgressBar.setVisibility(view.VISIBLE);

        // Floating action button
        nav = NavHostFragment.findNavController(this);
        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeListFragmentDirections.ActionNavigationRecipeListToNavigationNewRecipe action = RecipeListFragmentDirections.actionNavigationRecipeListToNavigationNewRecipe(pressed_category);
                action.setMessage(pressed_category);
                nav.navigate(action);

            }
        });
    }


    @Override
    public void onItemClick(int position) {

        Recipe r = data.get(position);
        RecipeListFragmentDirections.ActionNavigationRecipeListToNavigationRecipeDetails action = RecipeListFragmentDirections.actionNavigationRecipeListToNavigationRecipeDetails(r);
        action.setRecipeObj(r);
        nav.navigate(action);

    }

    @Override
    public void onLongItemClick(int position) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    interface OnItemClickListener{
        void onClick(int position);
    }

    static class RecipeRowViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtRecipeName;
        TextView txtRecipeDescription;
        ImageView img;
        Recipe recipe;

        public RecipeRowViewHolder(@NonNull View itemView, final OnItemClickListener listener)
        {
            super(itemView);
            img = itemView.findViewById(R.id.imageView);
            txtRecipeName = itemView.findViewById(R.id.txt_recipe_name);
            txtRecipeDescription = itemView.findViewById(R.id.txt_recipe_description);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Recipe r = data.get(position);
                    RecipeListFragmentDirections.ActionNavigationRecipeListToNavigationRecipeDetails action = RecipeListFragmentDirections.actionNavigationRecipeListToNavigationRecipeDetails(r);
                    action.setRecipeObj(r);
                    nav.navigate(action);
                }
            });

        }

        public void bind(Recipe r) {
            txtRecipeName.setText(r.name);
            txtRecipeDescription.setText(r.description);
            Picasso.get().load(R.drawable.ic_launcher_foreground).into(img);
            if(!r.getImgURL().equals(""))
            {
                Picasso.get().load(r.getImgURL()).into(img, new com.squareup.picasso.Callback(){

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
            }
            else
            {
                Picasso.get().load(R.drawable.ic_launcher_foreground).into(img);
            }
            recipe = r;
        }
    }

    class NewRecipesListAdapter extends RecyclerView.Adapter<RecipeRowViewHolder>
    {
        private OnItemClickListener listener;

        void setOnIntemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }


        @NonNull
        @Override
        public RecipeRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.row_recipe, viewGroup,false );
            RecipeRowViewHolder vh = new RecipeRowViewHolder(v, listener);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeRowViewHolder studentRowViewHolder, int i) {
            Recipe st = data.get(i);
            studentRowViewHolder.bind(st);

        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

}
