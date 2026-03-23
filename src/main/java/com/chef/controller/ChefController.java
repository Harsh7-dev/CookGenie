package com.chef.controller;

import com.chef.model.Recipe;
import com.chef.service.ChefService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/chef")
public class ChefController {

    private final ChefService chefService;

    public ChefController(ChefService chefService) {
        this.chefService = chefService;
    }

    public record ChatRequest(String message) {}

    @PostMapping("/chat")
    public String chat(@RequestBody ChatRequest request) {
        return chefService.chat(request.message());
    }

    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody ChatRequest request) {
        return chefService.chatStream(request.message());
    }

    @PostMapping("/recipe")
    public Recipe generateRecipe(@RequestBody ChatRequest request) {
        return chefService.generateRecipe(request.message());
    }
}