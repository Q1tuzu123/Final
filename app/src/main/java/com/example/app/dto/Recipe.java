package com.example.app.dto;

import java.util.ArrayList;

public class Recipe {

    public static class AnalyzedInstruction {
        public String name;
        public ArrayList<Step> steps;
    }

    public static class Equipment {
        public int id;
        public String name;
        public String localizedName;
        public String image;
    }

    public static class ExtendedIngredient {
        public int id;
        public String aisle;
        public String image;
        public String consistency;
        public String name;
        public String nameClean;
        public String original;
        public String originalName;
        public double amount;
        public String unit;
        public ArrayList<String> meta;
        public Measures measures;
    }

    public static class Ingredient {
        public int id;
        public String name;
        public String localizedName;
        public String image;
    }

    public static class Length {
        public int number;
        public String unit;
    }

    public static class Measures {
        public Us us;
        public Metric metric;
    }

    public static class Metric {
        public double amount;
        public String unitShort;
        public String unitLong;
    }

    public static class Root {
        public boolean vegetarian;
        public boolean vegan;
        public boolean glutenFree;
        public boolean dairyFree;
        public boolean veryHealthy;
        public boolean cheap;
        public boolean veryPopular;
        public boolean sustainable;
        public boolean lowFodmap;
        public int weightWatcherSmartPoints;
        public String gaps;
        public Object preparationMinutes;
        public Object cookingMinutes;
        public int aggregateLikes;
        public int healthScore;
        public String creditsText;
        public String license;
        public String sourceName;
        public double pricePerServing;
        public ArrayList<ExtendedIngredient> extendedIngredients;
        public int id;
        public String title;
        public int readyInMinutes;
        public int servings;
        public String sourceUrl;
        public String image;
        public String imageType;
        public String summary;
        public ArrayList<Object> cuisines;
        public ArrayList<String> dishTypes;
        public ArrayList<String> diets;
        public ArrayList<Object> occasions;
        public String instructions;
        public ArrayList<AnalyzedInstruction> analyzedInstructions;
        public Object originalId;
        public double spoonacularScore;
        public String spoonacularSourceUrl;
    }

    public static class Step {
        public int number;
        public String step;
        public ArrayList<Ingredient> ingredients;
        public ArrayList<Equipment> equipment;
        public Length length;
    }

    public static class Us {
        public double amount;
        public String unitShort;
        public String unitLong;
    }
}
