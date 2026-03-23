package com.chef.service;

import com.chef.model.Recipe;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChefService {

    private static final String SYSTEM_PROMPT = """
            You are Chef Claude, a world-class AI chef with expertise in cuisines from around the globe.
            You are enthusiastic, creative, and knowledgeable about cooking techniques, flavor pairings,
            and dietary accommodations.

            Your capabilities:
            - Generate detailed recipes with ingredients, steps, and timing
            - Suggest ingredient substitutions for dietary needs (use the ingredientSubstitution tool)
            - Provide nutritional information (use the nutritionLookup tool)
            - Scale recipes for different serving sizes (use the recipeScaling tool)
            - Adapt recipes for different skill levels
            - Suggest wine/beverage pairings
            - Explain cooking techniques

            When generating recipes, format them beautifully with clear sections.
            Use markdown formatting: **bold** for emphasis, headers for sections, and numbered lists for steps.
            Always be encouraging and make cooking feel accessible and fun!
            """;

    private static final String RECIPE_SYSTEM_PROMPT = """
            You are Chef Claude, a world-class AI chef. Generate a complete, detailed, and accurate recipe
            based on the user's request. Include precise measurements, clear step-by-step instructions,
            helpful chef's tips, appropriate tags (e.g., "vegetarian", "gluten-free", "quick", "comfort food"),
            and estimated nutrition per serving. Be thorough and professional.
            """;

    private final ChatClient chatClient;
    private final ChatClient recipeChatClient;

    public ChefService(
            OpenAiChatModel chatModel,
            FunctionToolCallback<NutritionService.NutritionRequest, NutritionService.NutritionResponse> nutritionLookup,
            FunctionToolCallback<SubstitutionService.SubstitutionRequest, SubstitutionService.SubstitutionResponse> ingredientSubstitution,
            FunctionToolCallback<RecipeScalingService.ScalingRequest, RecipeScalingService.ScalingResponse> recipeScaling) {

        this.chatClient = ChatClient.builder(chatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .defaultTools(nutritionLookup, ingredientSubstitution, recipeScaling)
                .build();

        this.recipeChatClient = ChatClient.builder(chatModel)
                .defaultSystem(RECIPE_SYSTEM_PROMPT)
                .defaultTools(nutritionLookup, ingredientSubstitution)
                .build();
    }

    public String chat(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }

    public Flux<String> chatStream(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .stream()
                .content();
    }

    public Recipe generateRecipe(String userMessage) {
        return recipeChatClient.prompt()
                .user(userMessage)
                .call()
                .entity(Recipe.class);
    }
}