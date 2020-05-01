package com.sid.foodrecipes.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sid.foodrecipes.AppExecutor;
import com.sid.foodrecipes.models.Recipe;
import com.sid.foodrecipes.requests.responses.RecipeResponse;
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
    private MutableLiveData<Recipe> mRecipe;
    private MutableLiveData<Boolean> mRecipeRequestTimeout = new MutableLiveData<>();
    private RetrieveRecipesRunnable mRetrieveRecipesRunnable;
    private RetrieveRecipeRunnable mRetrieveRecipeRunnable;

    public static RecipeApiClient getInstance(){
        if(mInstance == null){
            mInstance = new RecipeApiClient();
        }
        return mInstance;
    }

    private RecipeApiClient(){
        mRecipes = new MutableLiveData<>();
        mRecipe = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }
    public LiveData<Recipe> getRecipe(){
        return mRecipe;
    }
    public LiveData<Boolean> isRecipeRequestTimedOut(){
        return mRecipeRequestTimeout;
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
                //let the user know its time out
                handller.cancel(true);
                
            }
        },3000, TimeUnit.MILLISECONDS);

    }

    public void searchRecipeById(String recipeId){
        if(mRetrieveRecipeRunnable != null){
            mRetrieveRecipeRunnable = null;
        }
        mRetrieveRecipeRunnable = new RetrieveRecipeRunnable(recipeId);
        final Future handler = AppExecutor.getInstance().networkIO().submit(mRetrieveRecipeRunnable);

        mRecipeRequestTimeout.setValue(false);
        AppExecutor.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //let the user know its time out
                mRecipeRequestTimeout.postValue(true);
                handler.cancel(true);
            }
        },3000,TimeUnit.MILLISECONDS);
    }

    //retrive the recipes value from webservice using retrofit
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

    //retrive the recipe value based on Id from webservice using retrofit
    private class RetrieveRecipeRunnable implements Runnable{
        private String recipeId;
        private boolean cancelRequest;

        public RetrieveRecipeRunnable(String recipeId) {
            this.recipeId = recipeId;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipe(recipeId).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    Recipe recipe = ((RecipeResponse)response.body()).getRecipe();
                    mRecipe.postValue(recipe);
                }else {
                    String error = response.errorBody().string();
                    Log.d(TAG,"Error: "+error);
                    mRecipe.postValue(null);
                }
            } catch (IOException e) {
                Log.d(TAG,"Error: "+e.getMessage());
                mRecipe.postValue(null);
                e.printStackTrace();
            }

        }

        private Call<RecipeResponse> getRecipe(String recipeId){
            RecipeAPI recipeAPI = ServiceGenerator.getRecipeAPI();
            Call<RecipeResponse> recipeResponse = recipeAPI.getRecipeResponse(
                    Constants.API_KEY,
                    recipeId
            );
            return recipeResponse;
        }

        private void cancelRequest(){
            Log.d(TAG,"Cancel Request : Canceling the search request");
            cancelRequest = true;
        }
    }

    public void cancelRequest(){
        if (mRetrieveRecipesRunnable != null){
            mRetrieveRecipesRunnable.cancelRequest();
        }
        if (mRetrieveRecipeRunnable != null){
            mRetrieveRecipeRunnable.cancelRequest();
        }
    }
}
