package com.amicus.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {
    @GET("random.php")
    Call<RecipeResponce> getRandomRecipe();

    @GET("filter.php")
    Call<RecipeResponce> getReipesByIngredient(@Query("i")String ingredient);
}
