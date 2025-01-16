package com.amicus.myapplication;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_recipes")
public class FavoriteRecipe {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String imageUrl;

    public FavoriteRecipe(@NonNull String id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
