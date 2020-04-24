package com.sid.foodrecipes.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Recipe implements Parcelable {
    private String image_url;
    private float social_rank;
    private String publisher;
    private String source_url;
    private String recipe_id;
    private String publisher_url;
    private String title;
    private String[] ingredients;

    public Recipe(String image_url, float social_rank, String publisher, String source_url, String recipe_id,
                  String publisher_url, String title, String[] ingredients) {
        this.image_url = image_url;
        this.social_rank = social_rank;
        this.publisher = publisher;
        this.source_url = source_url;
        this.recipe_id = recipe_id;
        this.publisher_url = publisher_url;
        this.title = title;
        this.ingredients = ingredients;
    }

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        image_url = in.readString();
        social_rank = in.readFloat();
        publisher = in.readString();
        source_url = in.readString();
        recipe_id = in.readString();
        publisher_url = in.readString();
        title = in.readString();
        ingredients = in.createStringArray();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public float getSocial_rank() {
        return social_rank;
    }

    public void setSocial_rank(float social_rank) {
        this.social_rank = social_rank;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getPublisher_url() {
        return publisher_url;
    }

    public void setPublisher_url(String publisher_url) {
        this.publisher_url = publisher_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "image_url='" + image_url + '\'' +
                ", social_rank=" + social_rank +
                ", publisher='" + publisher + '\'' +
                ", source_url='" + source_url + '\'' +
                ", recipe_id='" + recipe_id + '\'' +
                ", publisher_url='" + publisher_url + '\'' +
                ", title='" + title + '\'' +
                ", ingredients=" + Arrays.toString(ingredients) +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image_url);
        dest.writeFloat(social_rank);
        dest.writeString(publisher);
        dest.writeString(source_url);
        dest.writeString(recipe_id);
        dest.writeString(publisher_url);
        dest.writeString(title);
        dest.writeStringArray(ingredients);
    }
}
