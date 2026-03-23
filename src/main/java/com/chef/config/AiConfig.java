package com.chef.config;

import com.chef.service.NutritionService;
import com.chef.service.RecipeScalingService;
import com.chef.service.SubstitutionService;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public FunctionToolCallback<NutritionService.NutritionRequest, NutritionService.NutritionResponse> nutritionLookup(
            NutritionService nutritionService) {
        return FunctionToolCallback
                .builder("nutritionLookup", nutritionService::lookupNutrition)
                .description("Look up nutritional information (calories, protein, carbs, fat, fiber) for a given ingredient. Use this when a user asks about nutrition or wants calorie counts.")
                .inputType(NutritionService.NutritionRequest.class)
                .build();
    }

    @Bean
    public FunctionToolCallback<SubstitutionService.SubstitutionRequest, SubstitutionService.SubstitutionResponse> ingredientSubstitution(
            SubstitutionService substitutionService) {
        return FunctionToolCallback
                .builder("ingredientSubstitution", substitutionService::findSubstitutes)
                .description("Find ingredient substitutions for dietary restrictions (vegan, gluten-free, dairy-free, etc). Use this when a user has allergies, dietary needs, or wants alternatives.")
                .inputType(SubstitutionService.SubstitutionRequest.class)
                .build();
    }

    @Bean
    public FunctionToolCallback<RecipeScalingService.ScalingRequest, RecipeScalingService.ScalingResponse> recipeScaling(
            RecipeScalingService recipeScalingService) {
        return FunctionToolCallback
                .builder("recipeScaling", recipeScalingService::scaleRecipe)
                .description("Scale a recipe's ingredient quantities from one serving size to another. Use this when a user wants to scale a recipe up or down (e.g., 'make this for 8 people instead of 4').")
                .inputType(RecipeScalingService.ScalingRequest.class)
                .build();
    }
}
