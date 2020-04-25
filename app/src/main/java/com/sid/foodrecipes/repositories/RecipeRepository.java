package com.sid.foodrecipes.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sid.foodrecipes.models.Recipe;
import com.sid.foodrecipes.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {
    private static RecipeRepository mInstance = null;
    private RecipeApiClient mRecipeApiClient;
    //private MutableLiveData<List<Recipe>> mRecipes;

    public static RecipeRepository getInstance(){
        if(mInstance == null){
            mInstance = new RecipeRepository();
        }
        return mInstance;
    }

    private RecipeRepository(){
        //mRecipes = new MutableLiveData<>();
        mRecipeApiClient = RecipeApiClient.getInstance();
    }

    public LiveData<List<Recipe>> getRecipe(){
        return mRecipeApiClient.getRecipe();
    }

    public void searchRecipeApi(String query, int pageNumber){
            if(pageNumber == 0){
                pageNumber = 1;
            }
            mRecipeApiClient.searchRecipesApi(query, pageNumber);
    }


}
