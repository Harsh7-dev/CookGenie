package com.chef.model;

public record NutritionInfo(
        int calories,
        int proteinGrams,
        int carbsGrams,
        int fatGrams,
        int fiberGrams
) {}
