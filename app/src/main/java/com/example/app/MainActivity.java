package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.dto.RecipeAdapter;
import com.example.app.dto.Result;
import com.example.app.dto.SpoonacularMedia;
import com.example.app.dto.client;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeClickListener {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Result> recipeList = new ArrayList<>();
    private ProgressBar progressBar;

    private static final String API_KEY = "54ca444fbe944911b717a6f4a5ef6a12"; // Replace with your actual API key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        recipeAdapter = new RecipeAdapter(recipeList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recipeAdapter);

        // Load default recipes when the app opens
        loadRecipes();
    }

    @Override
    public void onRecipeClick(int recipeId) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("RECIPE_ID", recipeId);
        startActivity(intent);
    }

    private void loadRecipes() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/recipes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        client apiClient = retrofit.create(client.class);

        // Call API without query for default recipes
        Call<SpoonacularMedia> call = apiClient.complexSearch(API_KEY);

        call.enqueue(new Callback<SpoonacularMedia>() {
            @Override
            public void onResponse(Call<SpoonacularMedia> call, Response<SpoonacularMedia> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    SpoonacularMedia media = response.body();
                    recipeList.clear();
                    recipeList.addAll(media.getResults());
                    recipeAdapter.notifyDataSetChanged();
                } else {
                    Log.e("MainActivity", "API Response failed or is null. Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<SpoonacularMedia> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("MainActivity", "API call failed: " + t.getMessage());
            }
        });
    }
}
