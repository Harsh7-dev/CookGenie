package com.chef.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NutritionService {

    private static final Map<String, int[]> NUTRITION_DB = Map.ofEntries(
            // [calories, protein, carbs, fat, fiber] per 100g
            Map.entry("chicken breast", new int[]{165, 31, 0, 4, 0}),
            Map.entry("salmon", new int[]{208, 20, 0, 13, 0}),
            Map.entry("rice", new int[]{130, 3, 28, 0, 0}),
            Map.entry("pasta", new int[]{131, 5, 25, 1, 2}),
            Map.entry("broccoli", new int[]{34, 3, 7, 0, 3}),
            Map.entry("avocado", new int[]{160, 2, 9, 15, 7}),
            Map.entry("egg", new int[]{155, 13, 1, 11, 0}),
            Map.entry("tofu", new int[]{76, 8, 2, 5, 0}),
            Map.entry("olive oil", new int[]{884, 0, 0, 100, 0}),
            Map.entry("potato", new int[]{77, 2, 17, 0, 2}),
            Map.entry("beef", new int[]{250, 26, 0, 15, 0}),
            Map.entry("shrimp", new int[]{99, 24, 0, 0, 0}),
            Map.entry("spinach", new int[]{23, 3, 4, 0, 2}),
            Map.entry("cheese", new int[]{402, 25, 1, 33, 0}),
            Map.entry("bread", new int[]{265, 9, 49, 3, 3})
    );

    public record NutritionRequest(String ingredient) {}
    public record NutritionResponse(String ingredient, int calories, int proteinGrams,
                                     int carbsGrams, int fatGrams, int fiberGrams, String note) {}

    public NutritionResponse lookupNutrition(NutritionRequest request) {
        String key = request.ingredient().toLowerCase().trim();
        for (var entry : NUTRITION_DB.entrySet()) {
            if (key.contains(entry.getKey()) || entry.getKey().contains(key)) {
                int[] vals = entry.getValue();
                return new NutritionResponse(request.ingredient(),
                        vals[0], vals[1], vals[2], vals[3], vals[4],
                        "Per 100g serving");
            }
        }
        return new NutritionResponse(request.ingredient(),
                0, 0, 0, 0, 0,
                "Nutrition data not found — estimate provided by AI");
    }
}
