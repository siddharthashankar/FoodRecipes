package com.sid.foodrecipes.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sid.foodrecipes.models.Recipe;
import com.sid.foodrecipes.repositories.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {
    private RecipeRepository mRecipeRepository;
    private boolean mIsViewingRecipies;

    public RecipeListViewModel(){
        mRecipeRepository = RecipeRepository.getInstance();
        mIsViewingRecipies = false;
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeRepository.getRecipe();
    }

    public void searchRecipeApi(String query, int pageNumber){
        mIsViewingRecipies = true;
        mRecipeRepository.searchRecipeApi(query, pageNumber);
    }

    public boolean isViewingRecipies() {
        return mIsViewingRecipies;
    }

    public void setIsViewingRecipies(boolean mIsViewingRecipies) {
        this.mIsViewingRecipies = mIsViewingRecipies;
    }

    public boolean onBackPressed(){
        if(mIsViewingRecipies){
            mIsViewingRecipies = false;
            return false;
        }
        return true;
    }
}
