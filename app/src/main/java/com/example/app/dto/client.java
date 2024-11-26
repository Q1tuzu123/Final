package com.example.app.dto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface client {
    //@GET("complexSearch")
    //Call<SpoonacularMedia> complexSearch(@Query("apiKey") String key);

    @GET("{id}/information")
    Call<Recipe.Root> getRecipeInformation(@Path("id") int id, @Query("apiKey") String apiKey);

    @GET("complexSearch")
    Call<SpoonacularMedia> complexSearch(
            @Query("apiKey") String key,
            @Query("query") String query
    );
}

