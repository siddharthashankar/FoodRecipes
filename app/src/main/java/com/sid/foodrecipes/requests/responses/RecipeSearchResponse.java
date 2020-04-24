package com.sid.foodrecipes.requests.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sid.foodrecipes.models.Recipe;

import java.util.List;

public class RecipeSearchResponse {
    @SerializedName("count")
    @Expose()
    private int count = 30;

    @SerializedName("recipes")
    @Expose()
    private List<Recipe> recipeList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @Override
    public String toString() {
        return "RecipeSearchResponse{" +
                "count=" + count +
                ", recipeList=" + recipeList +
                '}';
    }
}
