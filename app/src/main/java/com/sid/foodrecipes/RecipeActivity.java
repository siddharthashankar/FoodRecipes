package com.sid.foodrecipes;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sid.foodrecipes.models.Recipe;
import com.sid.foodrecipes.viewmodels.RecipeViewModel;


public class RecipeActivity extends BaseActivity {
    private static final String TAG = "RecipeActivity";
    // UI components
    private ImageView mRecipeImage;
    private TextView mRecipeTitle, mRecipeRank;
    private LinearLayout mRecipeIngredientsContainer;
    private ScrollView mScrollView;
    private RecipeViewModel mRecipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        mRecipeImage = findViewById(R.id.recipe_image);
        mRecipeTitle = findViewById(R.id.recipe_title);
        mRecipeRank = findViewById(R.id.recipe_social_score);
        mRecipeIngredientsContainer = findViewById(R.id.ingredients_container);
        mScrollView = findViewById(R.id.parent);

        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        showProgressBar(true);
        subscribeObserver();
        getIncomingIntent();

    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("recipe")) {
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            mRecipeViewModel.searchRecipeById(recipe.getRecipe_id());
        }
    }

    private void subscribeObserver(){
        mRecipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if(recipe != null){
                    if(recipe.getRecipe_id().equals(mRecipeViewModel.getRecipeId())){
                        setRecipeProperties(recipe);
                        mRecipeViewModel.setmDidRetrieveRecipe(true);
                    }
                }
            }
        });

        mRecipeViewModel.isRecipeRequestTimedOut().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean && !mRecipeViewModel.didRetrieveRecipe()){
                    Log.d(TAG,"onChanged: Timeout...");
                    displayErrorScreen("Error retrieving data. Check network connection.");
                }
            }
        });
    }

    private void displayErrorScreen(String errorMessage){
        mRecipeTitle.setText("Error retrieveing recipe...");
        mRecipeRank.setText("");
        TextView textView = new TextView(this);
        if(!errorMessage.equals("")){
            textView.setText(errorMessage);
        }
        else{
            textView.setText("Error");
        }
        textView.setTextSize(15);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        mRecipeIngredientsContainer.addView(textView);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(R.drawable.ic_launcher_background)
                .into(mRecipeImage);

        showParent();
        showProgressBar(false);
    }

    private void setRecipeProperties(Recipe recipe){
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(recipe.getImage_url())
                .into(mRecipeImage);
        mRecipeTitle.setText(recipe.getTitle());
        mRecipeRank.setText(String.valueOf(Math.round(recipe.getSocial_rank())));
        mRecipeIngredientsContainer.removeAllViews();
        for(String ingredeiants: recipe.getIngredients()){
            TextView textView = new TextView(this);
            textView.setText(ingredeiants);
            textView.setTextSize(15);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            mRecipeIngredientsContainer.addView(textView);
        }
        showParent();
        showProgressBar(false);
    }

    private void showParent(){
        mScrollView.setVisibility(View.VISIBLE);
    }

}
