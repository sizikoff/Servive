package com.amicus.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDAO {
    @Insert
    void insert(FavoriteRecipe recipe);

    @Query("SELECT * FROM favorite_recipes")
    List<FavoriteRecipe> getAll();

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE id =:id)")
    boolean isFavorite(String id);


    @Delete
    void delete(FavoriteRecipe recipe);
}
