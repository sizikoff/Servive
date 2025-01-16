package com.amicus.myapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteRecipe.class},version = 1)
public abstract class RecipeDatabase extends RoomDatabase {
    private static RecipeDatabase instance;
    public abstract RecipeDAO recipeDAO();

    public static synchronized RecipeDatabase getInstance(Context context){
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    RecipeDatabase.class,"recipe_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
