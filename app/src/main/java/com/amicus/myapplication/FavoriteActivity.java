package com.amicus.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    RecipeDatabase recipeDatabase;
    RecyclerView recyclerView;
    FavoriteAdapter favoriteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite);

        recipeDatabase = RecipeDatabase.getInstance(this);
        recyclerView = findViewById(R.id.favoriteRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadFavorite();
    }

    private void loadFavorite() {
        new Thread(() -> {
            List<FavoriteRecipe> favorites = recipeDatabase.recipeDAO().getAll();
            Log.d("FavoriteDebug", "Количество рецептов в базе: " + favorites.size());
            runOnUiThread(() -> {
                if (!favorites.isEmpty()) {
                    favoriteAdapter = new FavoriteAdapter(favorites);
                    recyclerView.setAdapter(favoriteAdapter);
                } else {
                    Toast.makeText(this, "Избранное пустое", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

}