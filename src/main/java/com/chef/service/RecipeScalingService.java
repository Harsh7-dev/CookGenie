package com.chef.service;

import org.springframework.stereotype.Service;

@Service
public class RecipeScalingService {

    public record ScalingRequest(String ingredientsText, int originalServings, int targetServings) {}
    public record ScalingResponse(double scalingFactor, String scalingNote, String cookingTip) {}

    public ScalingResponse scaleRecipe(ScalingRequest request) {
        double factor = (double) request.targetServings() / request.originalServings();

        String cookingTip;
        if (factor >= 3) {
            cookingTip = "For large batches: cooking time may increase 20-30%. Use a larger pan and stir more frequently. " +
                    "Baking times can increase significantly — check for doneness early.";
        } else if (factor >= 2) {
            cookingTip = "For doubled recipes: cooking time may increase 10-15%. Ensure your pan is large enough " +
                    "or cook in batches to avoid crowding.";
        } else if (factor <= 0.33) {
            cookingTip = "For very small batches: reduce cooking time significantly. Use smaller cookware for even cooking. " +
                    "Baked goods may be done 30-40% faster.";
        } else if (factor < 0.75) {
            cookingTip = "For reduced recipes: check doneness earlier than the original time. " +
                    "Use appropriately sized cookware to prevent burning.";
        } else {
            cookingTip = "Minimal adjustments needed. Cooking time should remain approximately the same.";
        }

        return new ScalingResponse(
                Math.round(factor * 100.0) / 100.0,
                String.format("Multiply all ingredient quantities by %.2f to serve %d instead of %d.",
                        factor, request.targetServings(), request.originalServings()),
                cookingTip
        );
    }
}