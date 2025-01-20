package com.amicus.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    TextView recipeName;
    TextView recipeInstructions;
    TextView recipeIngredients;
    ImageView recipeImage;
    Button refreshButton;
    Button searchButton;
    Button addToFavoriteButton;
    Button openFavoriteButton;
    Button shareButton;
    Button watchVideoButton;
    Button buyButton;

    RecipeDatabase recipeDatabase;
    Meal currentMeal;

    BillingProcessor bp;


    EditText ingredientInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recipeName = findViewById(R.id.recipeName);
        recipeInstructions = findViewById(R.id.recipeInstructions);
        recipeIngredients = findViewById(R.id.recipeIngredients);
        recipeImage = findViewById(R.id.recipeimage);
        refreshButton = findViewById(R.id.refreshButton);
        searchButton = findViewById(R.id.searchButton);
        ingredientInput = findViewById(R.id.ingredientInput);
        addToFavoriteButton = findViewById(R.id.addToFavoriteButton);
        openFavoriteButton = findViewById(R.id.openFavoriteButton);
        shareButton = findViewById(R.id.shareButton);
        watchVideoButton = findViewById(R.id.videoButton);
        buyButton = findViewById(R.id.buyButton);

        bp = new BillingProcessor(this,getString(R.string.LICENSE_KEY),this);
        bp.initialize();

        recipeDatabase = RecipeDatabase.getInstance(this);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bp.isInitialized()){
                    bp.purchase(MainActivity.this,"d1");
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentMeal != null) {
                    String shareText = "Попробуйте рецепт "+ currentMeal.getName()+"\nИнгредиенты:\n"+recipeIngredients.getText().toString()
                            +"\nИнструкция:"+currentMeal.getInstructions()+"\nВидео-рецепт:"+currentMeal.getVideoUrl();
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Рецепт");
                    shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
                    startActivity(Intent.createChooser(shareIntent,"Поделиться рецептом через"));
                }else {
                    Toast.makeText(MainActivity.this, R.string.recipe_not_found, Toast.LENGTH_SHORT).show();
                }
            }
        });

        watchVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentMeal != null&& currentMeal.getVideoUrl() !=null && !currentMeal.getVideoUrl().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentMeal.getVideoUrl()));
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "Видео недоступно для этого рецепта", Toast.LENGTH_SHORT).show();
                }
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchRandomRecipe();
            }
        });

        openFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(intent);
            }
        });

        addToFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentMeal != null) {
                    favorite();
                    Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Рецепт не загружен", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchRecipeByIngredients();
            }
        });
        fetchRandomRecipe();
    }

    private void favorite() {
        if (currentMeal != null) {
            Log.d("FavoriteDebug", "currentMeal ID: " + currentMeal.getId());
            Log.d("FavoriteDebug", "currentMeal Name: " + currentMeal.getName());
            Log.d("FavoriteDebug", "currentMeal ImageUrl: " + currentMeal.getImageUrl());
            FavoriteRecipe favoriteRecipe = new FavoriteRecipe(currentMeal.getId(), currentMeal.getName(), currentMeal.getImageUrl());
            new Thread(() -> {
                boolean exists = recipeDatabase.recipeDAO().isFavorite(favoriteRecipe.getId());
                if (!exists) {
                    recipeDatabase.recipeDAO().insert(favoriteRecipe);
                    runOnUiThread(() -> Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Уже в избранном", Toast.LENGTH_SHORT).show());
                }
            }).start();
        } else {
            Toast.makeText(this, "Рецепт не загружен", Toast.LENGTH_SHORT).show();
        }
    }


    private void fetchRandomRecipe() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeApi recipeApi = retrofit.create(RecipeApi.class);
        recipeApi.getRandomRecipe().enqueue(new Callback<RecipeResponce>() {
            @Override
            public void onResponse(Call<RecipeResponce> call, Response<RecipeResponce> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Meal meal = response.body().getMeals().get(0);

                    // Инициализируем currentMeal
                    currentMeal = meal;

                    // Обновляем UI
                    recipeName.setText(meal.getName());
                    recipeInstructions.setText(meal.getInstructions());
                    String ingredients = meal.getIngredient1() + "\n" + meal.getIngredient2()
                            + "\n" + meal.getIngredient3();
                    recipeIngredients.setText(ingredients);
                    Glide.with(MainActivity.this).load(meal.getImageUrl()).into(recipeImage);
                } else {
                    Toast.makeText(MainActivity.this, "Ошибка загрузки рецептов", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeResponce> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG",t.getMessage());
            }
        });
    }


    private void searchRecipeByIngredients(){
        String ingredient = ingredientInput.getText().toString().trim();
        if (ingredient.isEmpty()) {
            Toast.makeText(this, R.string.enter_ingredient, Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeApi recipeApi = retrofit.create(RecipeApi.class);
        recipeApi.getReipesByIngredient(ingredient).enqueue(new Callback<RecipeResponce>() {
            @Override
            public void onResponse(Call<RecipeResponce> call, Response<RecipeResponce> response) {
                if (response.isSuccessful()&& response.body() != null&& !response.body().getMeals().isEmpty()) {
                    displayRecipe(response.body().getMeals().get(0));
                }else {
                    Toast.makeText(MainActivity.this, "Рецептов с таким ингредиентом нет", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeResponce> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG",t.getMessage());
            }
        });
    }

    private void displayRecipe(Meal meal){
        recipeName.setText(meal.getName());
        recipeIngredients.setText("Ингредиенты " +meal.getIngredient1() +","+meal.getIngredient2()+
                ","+meal.getIngredient3());
        recipeInstructions.setText("Инструкция "+ meal.getInstructions());

        Glide.with(MainActivity.this).load(meal.getImageUrl()).into(recipeImage);

        if (meal.getVideoUrl() == null||meal.getVideoUrl().isEmpty()) {
            watchVideoButton.setVisibility(View.GONE);
        }else {
            watchVideoButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {
        if ("d1".equals(productId)){
            Toast.makeText(this, "Успешная покупка", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Toast.makeText(this, "Ошибка покупки "+errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    protected void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }
}