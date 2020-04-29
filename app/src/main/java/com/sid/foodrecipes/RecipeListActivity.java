package com.sid.foodrecipes;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.sid.foodrecipes.adapter.OnRecipeListener;
import com.sid.foodrecipes.adapter.RecipeRecyclerAdapter;
import com.sid.foodrecipes.models.Recipe;
import com.sid.foodrecipes.util.VerticalSpacingItemDecoration;
import com.sid.foodrecipes.viewmodels.RecipeListViewModel;
import java.util.List;


public class RecipeListActivity extends BaseActivity implements OnRecipeListener {
    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel mRecipeListViewModel;
    private RecyclerView rv;
    private RecipeRecyclerAdapter mAdapter;
    private SearchView mSearchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        rv = (RecyclerView)findViewById(R.id.rv);
        mSearchView = findViewById(R.id.searchView);
        //viewModel
        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        initRecyclerView();
        subscribeObservers();
        initSearchView();
        if(!mRecipeListViewModel.isViewingRecipies()){
            displaySearchCategory();
        }
        //testRetrofitRequest2();
    }

    private void subscribeObservers(){
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if(recipes != null){
                    if (mRecipeListViewModel.isViewingRecipies()){
                        mRecipeListViewModel.setIsPerformingQuery(false);
                        mAdapter.setRecipe(recipes);
                    }
                }
            }
        });
    }
    private void initRecyclerView(){
        mAdapter = new RecipeRecyclerAdapter(this);
        VerticalSpacingItemDecoration decoration = new VerticalSpacingItemDecoration(5);
        rv.addItemDecoration(decoration);
        rv.setAdapter(mAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
    /*private void searchRecipeApi(String query, int pageNumber){
        mRecipeListViewModel.searchRecipeApi(query, pageNumber);
    }*/
    private void initSearchView(){
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.displayLoading();
                mRecipeListViewModel.searchRecipeApi(query, 1);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
 /*   private void testRetrofitRequest2(){
        searchRecipeApi("chicken",1);
    }*/
    private void testRetrofitRequest(){
/*         RecipeAPI recipeAPI = ServiceGenerator.getRecipeAPI();

        // Test 1st API
       Call<RecipeSearchResponse> responseCall = recipeAPI.searchRecipe(Constants.API_KEY,"chicken","1");

        responseCall.enqueue(new Callback<RecipeSearchResponse>() {
            @Override
            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {
                Log.d(TAG,"Server Response: "+response.toString());
                if(response.code() == 200){
                   Log.d(TAG,"OnSuccessResponse: "+response.body().getCount());
                    List<Recipe> recipes = new ArrayList<>(response.body().getRecipeList());
                    for(Recipe recipe: recipes){
                        Log.d(TAG,"onResponse: "+recipe.getTitle());
                    }
                }
                else{
                    try{
                        Log.d(TAG,"Response Error: "+response.errorBody().toString());
                    }catch (Exception e){e.printStackTrace();}
                }

            }

            @Override
            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {
                Log.d(TAG,"Response Error2: "+t.getMessage());
            }
        });

        // Test 2nd API

        Call<RecipeResponse> responseCall = recipeAPI.getRecipeResponse(Constants.API_KEY,"41470");
        responseCall.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                Log.d(TAG,"OnResponse: "+response.body().toString());
                if(response.code() == 200){
                    Recipe recipes = response.body().getRecipe();
                    Log.d(TAG,"OnResponse: "+recipes.toString());
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {

            }
        });  */

    }

    @Override
    public void onRecipeClick(int id) {

    }

    @Override
    public void onCategoryClick(String category) {
        mAdapter.displayLoading();
        mRecipeListViewModel.searchRecipeApi(category, 1);
        mSearchView.clearFocus();
    }

    private void displaySearchCategory(){
        mRecipeListViewModel.setIsViewingRecipies(false);
        mAdapter.displaySearchCategory();
    }

    @Override
    public void onBackPressed() {
        if(mRecipeListViewModel.onBackPressed()){
            super.onBackPressed();
        }else {
            displaySearchCategory();
        }


    }
}
