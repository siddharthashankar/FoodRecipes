package com.sid.foodrecipes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sid.foodrecipes.R;
import com.sid.foodrecipes.models.Recipe;
import java.util.List;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Recipe> mRecipe;
    private OnRecipeListener onRecipeListener;

    public RecipeRecyclerAdapter(OnRecipeListener onRecipeListener) {
        this.onRecipeListener = onRecipeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recipe_list_item,viewGroup,false);
        return new RecipeViewHolder(view, onRecipeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);
        Glide.with(viewHolder.itemView.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(mRecipe.get(position).getImage_url())
                .into(((RecipeViewHolder)viewHolder).img);

        ((RecipeViewHolder)viewHolder).title.setText(mRecipe.get(position).getTitle());
        ((RecipeViewHolder)viewHolder).publisher.setText(mRecipe.get(position).getPublisher());
        ((RecipeViewHolder)viewHolder).socialScore.setText(String.valueOf(Math.round(mRecipe.get(position).getSocial_rank())));
    }

    @Override
    public int getItemCount() {
        if (mRecipe != null){
            return mRecipe.size();
        }
       return 0;
    }

    public void setRecipe(List<Recipe> recipe){
        this.mRecipe = recipe;
        notifyDataSetChanged();
    }
}
