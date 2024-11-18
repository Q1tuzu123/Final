package com.example.app.dto;

import java.util.List;

public class SpoonacularMedia {
    private List<Result> results; // This will map to the "results" field in your JSON response
    private int offset;
    private int number;
    private int totalResults;

    // Getter for the results list
    public List<Result> getResults() {
        return results;
    }

    // Other getters if needed
    public int getOffset() {
        return offset;
    }

    public int getNumber() {
        return number;
    }

    public int getTotalResults() {
        return totalResults;
    }
}
