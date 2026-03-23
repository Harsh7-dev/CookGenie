package com.chef.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SubstitutionService {

    private static final Map<String, List<String>> SUBSTITUTIONS = Map.ofEntries(
            Map.entry("butter", List.of("coconut oil", "olive oil", "applesauce (for baking)", "avocado")),
            Map.entry("eggs", List.of("flax egg (1 tbsp ground flax + 3 tbsp water)", "chia egg", "mashed banana", "silken tofu")),
            Map.entry("milk", List.of("oat milk", "almond milk", "coconut milk", "soy milk")),
            Map.entry("cream", List.of("coconut cream", "cashew cream", "silken tofu blended", "evaporated milk")),
            Map.entry("flour", List.of("almond flour", "coconut flour", "oat flour", "rice flour")),
            Map.entry("sugar", List.of("honey", "maple syrup", "stevia", "coconut sugar")),
            Map.entry("soy sauce", List.of("coconut aminos", "tamari (gluten-free)", "liquid aminos", "fish sauce")),
            Map.entry("cheese", List.of("nutritional yeast", "cashew cheese", "tofu ricotta", "vegan cheese")),
            Map.entry("beef", List.of("mushrooms", "lentils", "jackfruit", "tempeh")),
            Map.entry("chicken", List.of("tofu", "seitan", "chickpeas", "cauliflower"))
    );

    public record SubstitutionRequest(String ingredient, String dietaryRestriction) {}
    public record SubstitutionResponse(String originalIngredient, List<String> substitutes, String tip) {}

    public SubstitutionResponse findSubstitutes(SubstitutionRequest request) {
        String key = request.ingredient().toLowerCase().trim();
        for (var entry : SUBSTITUTIONS.entrySet()) {
            if (key.contains(entry.getKey()) || entry.getKey().contains(key)) {
                return new SubstitutionResponse(
                        request.ingredient(),
                        entry.getValue(),
                        "Substitution ratios may vary. Adjust to taste!"
                );
            }
        }
        return new SubstitutionResponse(
                request.ingredient(),
                List.of("No common substitutes found — ask the AI chef for ideas!"),
                "The AI chef can suggest creative alternatives."
        );
    }
}
