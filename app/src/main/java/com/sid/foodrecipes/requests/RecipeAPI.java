package com.sid.foodrecipes.requests;

import com.sid.foodrecipes.requests.responses.RecipeResponse;
import com.sid.foodrecipes.requests.responses.RecipeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeAPI {
    //Search request
    @GET("api/search")
    Call<RecipeSearchResponse> searchRecipe(
                @Query("key") String key,
                @Query("q") String query,
                @Query("page") String page
    );

    //Recipe Request
    @GET("api/get")
    Call<RecipeResponse> getRecipeResponse(
                @Query("key") String key,
                @Query("rId") String response_id
    );




}
