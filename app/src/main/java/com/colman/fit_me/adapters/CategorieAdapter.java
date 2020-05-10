package com.colman.fit_me.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.colman.fit_me.R;
import com.colman.fit_me.RecyclerViewClickInterface;
import com.colman.fit_me.model.Category;
import com.colman.fit_me.ui.ex_item.ExItemdFragment;

import java.util.ArrayList;


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class CategorieAdapter extends RecyclerView.Adapter<CategorieAdapter.CategorieViewHolder>
{
    private ArrayList<Category> dataList;
    private RecyclerViewClickInterface recyclerViewClickInterface;



    public CategorieAdapter(ArrayList<Category> dataList)
    {
        this.dataList = dataList;
    }


    public CategorieAdapter(ArrayList<Category> dataList, RecyclerViewClickInterface recyclerViewClickInterface)
    {
        this.dataList = dataList;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }


    @Override
    public CategorieViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_categorie, parent, false);
        return new CategorieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategorieViewHolder holder, int position)
    {
        holder.txtCategorieName.setText(dataList.get(position).getName());
    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }

    /////////////////////////
    /// View Holder Class ///
    /////////////////////////

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    class CategorieViewHolder extends RecyclerView.ViewHolder
    {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private int mCurrentPosition;
        TextView txtCategorieName;
        CardView myCardView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public CategorieViewHolder(View itemView)
        {
            super(itemView);
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.

            txtCategorieName = (TextView) itemView.findViewById(R.id.txt_categorie_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());

                }
            });

        }

    }


}
