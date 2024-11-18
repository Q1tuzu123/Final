package com.example.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.app.dto.Recipe;
import com.example.app.dto.client;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeDetailActivity extends AppCompatActivity {
    private TextView titleTextView;
    private TextView ingredientsTextView;
    private TextView instructionsTextView;
    private ImageView recipeImageView;
    private ProgressBar progressBar;

    private static final String API_KEY = "54ca444fbe944911b717a6f4a5ef6a12"; // Your actual API key
    private int recipeId; // Recipe ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Find views by their IDs
        titleTextView = findViewById(R.id.titleTextView);
        ingredientsTextView = findViewById(R.id.ingredientsListTextView);
        instructionsTextView = findViewById(R.id.instructionsListTextView);
        recipeImageView = findViewById(R.id.recipeImageView);
        progressBar = findViewById(R.id.progressBar);

        // Add references for the new titles
        TextView ingredientsTitle = findViewById(R.id.ingredientsTitle);
        TextView instructionsTitle = findViewById(R.id.instructionsTitle);

        // Set static titles
        ingredientsTitle.setText("Ingredients");
        instructionsTitle.setText("Instructions");

        // Get the recipe ID from the intent
        recipeId = getIntent().getIntExtra("RECIPE_ID", -1);

        if (recipeId == -1) {
            Toast.makeText(this, "Invalid recipe ID!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Load recipe details
            loadRecipeDetails(recipeId);
        }
    }

    private void loadRecipeDetails(int recipeId) {
        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/recipes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the API client
        client apiClient = retrofit.create(client.class);

        // Call the method to get recipe information
        Call<Recipe.Root> call = apiClient.getRecipeInformation(recipeId, API_KEY);

        call.enqueue(new Callback<Recipe.Root>() {
            @Override
            public void onResponse(Call<Recipe.Root> call, Response<Recipe.Root> response) {
                progressBar.setVisibility(View.GONE); // Hide progress bar

                if (response.isSuccessful() && response.body() != null) {
                    Recipe.Root recipeDetails = response.body();
                    // Display the recipe details
                    titleTextView.setText(recipeDetails.title);
                    ingredientsTextView.setText(getIngredientsString(recipeDetails.extendedIngredients));
                    instructionsTextView.setText(recipeDetails.instructions);
                    Glide.with(RecipeDetailActivity.this).load(recipeDetails.image).into(recipeImageView);
                } else {
                    Log.e("RecipeDetailActivity", "API Response failed or is null.");
                }
            }

            @Override
            public void onFailure(Call<Recipe.Root> call, Throwable t) {
                progressBar.setVisibility(View.GONE); // Hide progress bar
                Log.e("RecipeDetailActivity", "API call failed: " + t.getMessage());
            }
        });
    }

    private String getIngredientsString(ArrayList<Recipe.ExtendedIngredient> ingredients) {
        StringBuilder ingredientsString = new StringBuilder();
        for (Recipe.ExtendedIngredient ingredient : ingredients) {
            // Add the measurement and ingredient name
            ingredientsString.append("• ") // Add a bullet point
                    .append(ingredient.amount) // Amount
                    .append(" ")
                    .append(ingredient.unit) // Unit
                    .append(" - ")
                    .append(ingredient.name) // Ingredient name
                    .append("\n"); // Newline for each item
        }
        return ingredientsString.toString();
    }
}
