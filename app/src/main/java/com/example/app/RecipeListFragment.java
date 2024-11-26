package com.example.app;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.dto.client;
import com.example.app.dto.Result;
import com.example.app.dto.RecipeAdapter;
import com.example.app.dto.SpoonacularMedia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListFragment extends Fragment {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private RecipeAdapter recipeAdapter;
    private List<Result> recipeList = new ArrayList<>();
    private client apiClient;
    private static final String API_KEY = "54ca444fbe944911b717a6f4a5ef6a12"; // Replace with your actual API key


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeAdapter = new RecipeAdapter(recipeList, recipe -> openRecipeDetails(recipe));
        recyclerView.setAdapter(recipeAdapter);

        apiClient = ApiClient.getClient().create(client.class);

        fetchRecipes("");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchRecipes(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    private void fetchRecipes(String ingredients) {
        Call<SpoonacularMedia> call = apiClient.complexSearch(API_KEY, ingredients);
        Log.d("Retrofit URL", call.request().url().toString());

        call.enqueue(new Callback<SpoonacularMedia>() {
            @Override
            public void onResponse(Call<SpoonacularMedia> call, Response<SpoonacularMedia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipeList.clear();
                    recipeList.addAll(response.body().getResults());
                    recipeAdapter.notifyDataSetChanged();
                } else {
                    // Log detailed error information
                    Log.e("API", "Response failed. Code: " + response.code());
                    if (response.errorBody() != null) {
                        try {
                            Log.e("API", "Error Body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getContext(), "Failed to load recipes. Check API key or query.", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<SpoonacularMedia> call, Throwable t) {
                Log.e("API", "Error: " + t.getMessage());
            }
        });
    }

    private void openRecipeDetails(Result recipe) {
        int recipeId = recipe.getId(); // Extract the recipe ID
        RecipeDetailFragment fragment = RecipeDetailFragment.newInstance(recipeId);

        // Navigate to RecipeDetailFragment
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null) // Enable back navigation
                .commit();
    }

}

