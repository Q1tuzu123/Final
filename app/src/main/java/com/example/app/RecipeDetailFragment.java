package com.example.app;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.app.dto.Recipe;
import com.example.app.dto.client;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeDetailFragment extends Fragment {

    private TextView titleTextView;
    private TextView ingredientsTextView;
    private TextView instructionsTextView;
    private ImageView recipeImageView;
    private ProgressBar progressBar;

    private static final String ARG_RECIPE_ID = "RECIPE_ID";
    private static final String API_KEY = "54ca444fbe944911b717a6f4a5ef6a12"; // Your actual API key
    private int recipeId; // Recipe ID

    public static RecipeDetailFragment newInstance(int recipeId) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeId = getArguments().getInt(ARG_RECIPE_ID, -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        // Find views
        titleTextView = view.findViewById(R.id.titleTextView);
        ingredientsTextView = view.findViewById(R.id.ingredientsListTextView);
        instructionsTextView = view.findViewById(R.id.instructionsListTextView);
        recipeImageView = view.findViewById(R.id.recipeImageView);
        progressBar = view.findViewById(R.id.progressBar);

        // Add references for titles
        TextView ingredientsTitle = view.findViewById(R.id.ingredientsTitle);
        TextView instructionsTitle = view.findViewById(R.id.instructionsTitle);

        // Set static titles
        ingredientsTitle.setText("Ingredients");
        instructionsTitle.setText("Instructions");

        // Load recipe details
        if (recipeId == -1) {
            Toast.makeText(getContext(), "Invalid recipe ID!", Toast.LENGTH_SHORT).show();
        } else {
            loadRecipeDetails(recipeId);
        }

        return view;
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
                    Glide.with(requireContext()).load(recipeDetails.image).into(recipeImageView);
                } else {
                    Log.e("RecipeDetailFragment", "API Response failed or is null.");
                }
            }

            @Override
            public void onFailure(Call<Recipe.Root> call, Throwable t) {
                progressBar.setVisibility(View.GONE); // Hide progress bar
                Log.e("RecipeDetailFragment", "API call failed: " + t.getMessage());
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



