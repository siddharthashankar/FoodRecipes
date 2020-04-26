package com.sid.foodrecipes.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sid.foodrecipes.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView title, publisher, socialScore;
    ImageView img;
    OnRecipeListener onRecipeListener;
    public RecipeViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener) {
        super(itemView);
        this.onRecipeListener = onRecipeListener;
        title = (TextView) itemView.findViewById(R.id.recipe_title);
        publisher = (TextView) itemView.findViewById(R.id.recipe_publisher);
        socialScore = (TextView) itemView.findViewById(R.id.recipe_social_score);
        img = (ImageView) itemView.findViewById(R.id.recipe_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onRecipeListener.onRecipeClick(getAdapterPosition());
    }
}
