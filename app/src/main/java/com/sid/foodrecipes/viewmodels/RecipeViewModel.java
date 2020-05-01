package com.sid.foodrecipes.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sid.foodrecipes.models.Recipe;
import com.sid.foodrecipes.repositories.RecipeRepository;

public class RecipeViewModel extends ViewModel {
    private RecipeRepository mRecipeRepository;
    private String mRecipeId;
    private boolean mDidRetrieveRecipe;

    public RecipeViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mDidRetrieveRecipe = false;
    }

    public LiveData<Recipe> getRecipe(){
       return mRecipeRepository.getRecipe();
    }
    public LiveData<Boolean> isRecipeRequestTimedOut(){
        return mRecipeRepository.isRecipeRequestTimedOut();
    }
    public void searchRecipeById(String recipeId){
        mRecipeId = recipeId;
        mRecipeRepository.searchRecipeById(recipeId);
    }
    public String getRecipeId(){
        return mRecipeId;
    }
    public void setmDidRetrieveRecipe(boolean didRetrieveRecipe){
        mDidRetrieveRecipe = didRetrieveRecipe;
    }
    public Boolean didRetrieveRecipe(){
        return mDidRetrieveRecipe;
    }
}
