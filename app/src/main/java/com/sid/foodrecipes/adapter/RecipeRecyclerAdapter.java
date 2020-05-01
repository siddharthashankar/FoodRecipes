package com.sid.foodrecipes.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sid.foodrecipes.R;
import com.sid.foodrecipes.models.Recipe;
import com.sid.foodrecipes.util.Constants;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int RECIPE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int CATEGORY_TYPE = 3;
    private static final int EXHAUSTED_TYPE = 4;

    private List<Recipe> mRecipe;
    private OnRecipeListener onRecipeListener;

    public RecipeRecyclerAdapter(OnRecipeListener onRecipeListener) {
        this.onRecipeListener = onRecipeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = null;

        switch (viewType) { // i is the view type constant
            case RECIPE_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recipe_list_item, viewGroup, false);
                return new RecipeViewHolder(view, onRecipeListener);
            }

            case LOADING_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading_list_item, viewGroup, false);
                return new LoadingViewHolder(view);
            }

            case EXHAUSTED_TYPE:{
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_search_exhausted, viewGroup, false);
                return new SearchExhaustedViewHolder(view);
            }

            case CATEGORY_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_category_list_item, viewGroup, false);
                return new CategoryViewHolder(view, onRecipeListener);
            }

            default: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recipe_list_item, viewGroup, false);
                return new RecipeViewHolder(view, onRecipeListener);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == RECIPE_TYPE) {
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);
            Glide.with(viewHolder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(mRecipe.get(position).getImage_url())
                    .into(((RecipeViewHolder) viewHolder).img);

            ((RecipeViewHolder) viewHolder).title.setText(mRecipe.get(position).getTitle());
            ((RecipeViewHolder) viewHolder).publisher.setText(mRecipe.get(position).getPublisher());
            ((RecipeViewHolder) viewHolder).socialScore.setText(String.valueOf(Math.round(mRecipe.get(position).getSocial_rank())));
        } else if (itemViewType == CATEGORY_TYPE) {
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);
            Uri path = Uri.parse("android.resource://com.sid.foodrecipes/drawable/" + mRecipe.get(position).getImage_url());
            Glide.with(viewHolder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(path)
                    .into(((CategoryViewHolder) viewHolder).circleImageView);

            ((CategoryViewHolder) viewHolder).categoryTitle.setText(mRecipe.get(position).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        if (mRecipe != null) {
            return mRecipe.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mRecipe.get(position).getSocial_rank() == -1) {
            return CATEGORY_TYPE;
        } else if (mRecipe.get(position).getTitle().equals("LOADING...")) {
            return LOADING_TYPE;
        } else if(mRecipe.get(position).getTitle().equals("EXHAUSTED...")){
            return EXHAUSTED_TYPE;
        }
        else if (position == mRecipe.size()-1 && position != 0 && ! mRecipe.get(position).getTitle().equals("EXHAUSTED...")){
            return LOADING_TYPE;
        }
        else {
            return RECIPE_TYPE;
        }
    }

    public void setQueryExhausted(){
        hideLoading();
        Recipe exhaustedRecipe = new Recipe();
        exhaustedRecipe.setTitle("EXHAUSTED...");
        mRecipe.add(exhaustedRecipe);
        notifyDataSetChanged();
    }

    private void hideLoading(){
        if(isLoading()){
            for(Recipe recipe: mRecipe){
                if(recipe.getTitle().equals("LOADING...")){
                    mRecipe.remove(recipe);
                }
            }
            notifyDataSetChanged();
        }
    }

    public void displayLoading() {
        if (!isLoading()) {
            Recipe recipe = new Recipe();
            recipe.setTitle("LOADING...");
            List<Recipe> loadingList = new ArrayList<>();
            loadingList.add(recipe);
            mRecipe = loadingList;
            notifyDataSetChanged();
        }
    }

    private boolean isLoading() {
        if (mRecipe != null) {
            if (mRecipe.size() > 0) {
                if (mRecipe.get(mRecipe.size() - 1).getTitle().equals("LOADING...")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void displaySearchCategory(){
        List<Recipe> categories = new ArrayList<>();
        for (int i = 0; i< Constants.DEFAULT_SEARCH_CATEGORIES.length; i++){
            Recipe recipe = new Recipe();
            recipe.setTitle(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
            recipe.setImage_url(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            recipe.setSocial_rank(-1);
            categories.add(recipe);
        }
        mRecipe = categories;
        notifyDataSetChanged();
    }

    public void setRecipe(List<Recipe> recipe) {
        this.mRecipe = recipe;
        notifyDataSetChanged();
    }

    public Recipe getSelectedRecipe(int position){
        if(mRecipe != null){
            if(mRecipe.size() > 0){
                return mRecipe.get(position);
            }
        }
        return null;
    }
}
