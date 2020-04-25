package com.sid.foodrecipes.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sid.foodrecipes.AppExecutor;
import com.sid.foodrecipes.models.Recipe;
import com.sid.foodrecipes.requests.responses.RecipeSearchResponse;
import com.sid.foodrecipes.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class RecipeApiClient {
    private static final String TAG = "RecipeApiClient";
    private static RecipeApiClient mInstance = null;
    private MutableLiveData<List<Recipe>> mRecipes;
    private RetrieveRecipesRunnable mRetrieveRecipesRunnable;
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

    public void searchRecipesApi(String query, int pageNumber){
        if(mRetrieveRecipesRunnable != null) {
            mRetrieveRecipesRunnable = null;
        }
        mRetrieveRecipesRunnable = new RetrieveRecipesRunnable(query, pageNumber);
        final Future handller = AppExecutor.getInstance().networkIO().submit(mRetrieveRecipesRunnable);

        AppExecutor.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                handller.cancel(true);
                
            }
        },3000, TimeUnit.MILLISECONDS);

    }

    //retrive the value from webservice using retrofit
    private class RetrieveRecipesRunnable implements Runnable{
        private String query;
        private int pageNumber;
        private boolean cancelRequest;

        public RetrieveRecipesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipes(query,pageNumber).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse)response.body()).getRecipeList());
                    if(pageNumber == 1){
                        mRecipes.postValue(list);
                    }else {
                        List<Recipe> currentRecipes = mRecipes.getValue();
                        currentRecipes.addAll(list);
                        mRecipes.postValue(currentRecipes);
                    }
                }else {
                    String error = response.errorBody().string();
                    Log.d(TAG,"Error: "+error);
                    mRecipes.postValue(null);
                }
            } catch (IOException e) {
                Log.d(TAG,"Error: "+e.getMessage());
                mRecipes.postValue(null);
                e.printStackTrace();
            }

        }

        private Call<RecipeSearchResponse> getRecipes(String query, int pageNumber){
                RecipeAPI recipeAPI = ServiceGenerator.getRecipeAPI();
                Call<RecipeSearchResponse> recipeSearchResponse = recipeAPI.searchRecipe(
                        Constants.API_KEY,
                        query,
                        String.valueOf(pageNumber)
                );
            return recipeSearchResponse;
        }

        private void cancelRequest(){
            Log.d(TAG,"Cancel Request : Canceling the search request");
            cancelRequest = true;
        }
    }
}
