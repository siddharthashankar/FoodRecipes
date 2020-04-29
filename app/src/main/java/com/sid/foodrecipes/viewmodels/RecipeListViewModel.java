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
    private boolean isPerformingQuery;

    public RecipeListViewModel(){
        mRecipeRepository = RecipeRepository.getInstance();
        mIsViewingRecipies = false;
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeRepository.getRecipe();
    }

    public void searchRecipeApi(String query, int pageNumber){
        mIsViewingRecipies = true;
        isPerformingQuery = true;
        mRecipeRepository.searchRecipeApi(query, pageNumber);
    }

    public boolean isViewingRecipies() {
        return mIsViewingRecipies;
    }

    public void setIsViewingRecipies(boolean mIsViewingRecipies) {
        this.mIsViewingRecipies = mIsViewingRecipies;
    }

    public void searchNextPage(){
        if (!isPerformingQuery && mIsViewingRecipies){
            mRecipeRepository.searchNextPage();
        }
    }

    public boolean onBackPressed(){
        if(isPerformingQuery){
            mRecipeRepository.cancelRequest();
            isPerformingQuery = false;
        }
        else if(mIsViewingRecipies){
            mIsViewingRecipies = false;
            return false;
        }
        return true;
    }

    public boolean isPerformingQuery() {
        return isPerformingQuery;
    }

    public void setIsPerformingQuery(boolean performingQuery) {
        isPerformingQuery = performingQuery;
    }
}
