package com.chef.model;

import java.util.List;

public record Recipe(
        String name,
        String description,
        String cuisine,
        String difficulty,
        int servings,
        int prepTimeMinutes,
        int cookTimeMinutes,
        List<String> ingredients,
        List<String> instructions,
        List<String> tips,
        List<String> tags,
        NutritionInfo nutritionInfo
) {}
