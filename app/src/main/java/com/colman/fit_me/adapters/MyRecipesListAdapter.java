package com.colman.fit_me.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.colman.fit_me.R;
import com.colman.fit_me.RecyclerViewClickInterface;
import com.colman.fit_me.model.Recipe;

import java.util.List;


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class MyRecipesListAdapter extends RecyclerView.Adapter<MyRecipesListAdapter.MyRecipesViewHolder> {

    private RecyclerViewClickInterface recyclerViewClickInterface;
    private List<Recipe> mRecipes; // Cached copy of recipes

    public MyRecipesListAdapter(List<Recipe> mRecipes, RecyclerViewClickInterface recyclerViewClickInterface)
    {
        this.mRecipes = mRecipes;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }


    public MyRecipesListAdapter(Context context)
    {
        //this.recyclerViewClickInterface = recyclerViewClickInterface;
        //mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyRecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //View itemView = mInflater.inflate(R.layout.row_categorie, parent, false);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_recipe, parent, false);
        return new MyRecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyRecipesViewHolder holder, int position) {
        if (mRecipes != null)
        {
            Recipe current = mRecipes.get(position);
            holder.txtRecipeName.setText(current.getName());
        }
        else
        {
            // Covers the case of data not being ready yet.
            holder.txtRecipeName.setText("No Recipe");
        }
    }

    public void setRecipes(List<Recipe> recipes){
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mRecipes has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mRecipes != null)
            return mRecipes.size();
        else return 0;
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    class MyRecipesViewHolder extends RecyclerView.ViewHolder
    {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private int mCurrentPosition;
        TextView txtRecipeName;
        CardView myCardView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public MyRecipesViewHolder(View itemView)
        {
            super(itemView);
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.

            txtRecipeName = (TextView) itemView.findViewById(R.id.txt_recipe_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // When clicking specific Recipe inside Category
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());

                }
            });

        }

    }
}