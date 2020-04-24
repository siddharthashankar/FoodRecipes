package com.sid.foodrecipes.requests;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sid.foodrecipes.models.Recipe;

import java.util.List;

public class RecipeApiClient {
    private static RecipeApiClient mInstance = null;
    private MutableLiveData<List<Recipe>> mRecipes;

    public static RecipeApiClient getInstance(){
        if(mInstance == null){
            mInstance = new RecipeApiClient();
        }
        return mInstance;
    }

    private RecipeApiClient(){
        mRecipes = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipe(){
        return mRecipes;
    }
}
