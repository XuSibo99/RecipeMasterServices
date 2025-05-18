package project.api.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import project.api.model.aisuggestion.DietaryRestriction;
import project.api.model.aisuggestion.RecipeSummary;
import project.api.model.aisuggestion.RecipeFull;

@Service
public class SpoonacularClientService {

    private final WebClient webClient;
    private final String spoonacularApiKey;

    public SpoonacularClientService(
            WebClient.Builder webClientBuilder,
            @Value("${spoonacular.api.key}") String spoonacularApiKey) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.spoonacular.com")
                .build();
        this.spoonacularApiKey = spoonacularApiKey;
    }

    public List<RecipeSummary> searchRecipes(
            String query,
            List<DietaryRestriction> restrictions,
            int number) {

        String dietParam = restrictions.stream()
                .map(DietaryRestriction::name)
                .map(String::toLowerCase)
                .collect(Collectors.joining(","));

        ComplexSearchResponse response = webClient.get()
                .uri(uriBuilder -> {
                    var b = uriBuilder
                            .path("/recipes/complexSearch")
                            .queryParam("apiKey", spoonacularApiKey)
                            .queryParam("query", query)
                            .queryParam("number", number)
                            .queryParam("addRecipeInformation", true)
                            .queryParam("addRecipeNutrition", true);
                    if (!dietParam.isEmpty()) {
                        b = b.queryParam("diet", dietParam);
                    }
                    return b.build();
                })
                .retrieve()
                .bodyToMono(ComplexSearchResponse.class)
                .block();

        return response.results().stream()
                .map(r -> new RecipeSummary(
                        r.id(),
                        r.title(),
                        r.image(),
                        r.sourceUrl(),
                        r.spoonacularSourceUrl()))
                .collect(Collectors.toList());
    }

    public RecipeFull getRecipe(Long id) {
        RecipeInfoResponse info = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/recipes/{id}/information")
                        .queryParam("apiKey", spoonacularApiKey)
                        .queryParam("includeNutrition", true)
                        .build(id))
                .retrieve()
                .bodyToMono(RecipeInfoResponse.class)
                .block();

        if (info == null) {
            throw new RuntimeException("Failed to fetch recipe info for id " + id);
        }

        double calories = info.nutrition().nutrients().stream()
                .filter(n -> "Calories".equalsIgnoreCase(n.name()))
                .findFirst()
                .map(n -> n.amount())
                .orElse(0.0);

        List<RecipeFull.Instruction> steps = info.analyzedInstructions().stream()
                .map(step -> new RecipeFull.Instruction(
                        step.step(),
                        step.ingredients().stream()
                                .map(i -> new RecipeFull.IngredientRef(i.id().toString(), i.name()))
                                .collect(Collectors.toList()),
                        step.equipment().stream()
                                .map(e -> new RecipeFull.EquipmentRef(e.id().toString(), e.name()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());

        return new RecipeFull(
                info.id(),
                info.title(),
                info.image(),
                info.sourceUrl(),
                info.spoonacularSourceUrl(),
                info.healthScore(),
                calories,
                info.dishTypes(),
                info.cuisines(),
                info.readyInMinutes(),
                info.servings(),
                info.vegetarian(),
                info.vegan(),
                info.glutenFree(),
                info.dairyFree(),
                info.summary(),
                info.instructions(),
                steps,
                new RecipeFull.Nutrition(
                        info.nutrition().nutrients().stream()
                                .map(n -> new RecipeFull.Nutrient(n.name(), n.amount(), n.unit()))
                                .collect(Collectors.toList())));
    }

    public record ComplexSearchResponse(List<SearchResult> results) {
    }

    public record SearchResult(
            Long id,
            String title,
            String image,
            Integer readyInMinutes,
            Integer servings,
            String sourceUrl,
            String spoonacularSourceUrl,
            Nutrition nutrition) {
    }

    public record Nutrition(List<Nutrient> nutrients) {
    }

    public record Nutrient(String name, Double amount, String unit) {
    }

    public record RecipeInfoResponse(
            Long id,
            String title,
            String image,
            String sourceUrl,
            String spoonacularSourceUrl,
            Integer healthScore,
            List<String> dishTypes,
            List<String> cuisines,
            Integer readyInMinutes,
            Integer servings,
            Boolean vegetarian,
            Boolean vegan,
            Boolean glutenFree,
            Boolean dairyFree,
            String summary,
            String instructions,
            List<InstructionStep> analyzedInstructions,
            Nutrition nutrition) {
        public record InstructionStep(
                String step,
                List<Ingredient> ingredients,
                List<Equipment> equipment) {
        }

        public record Ingredient(
                Long id,
                String name) {
        }

        public record Equipment(
                Long id,
                String name) {
        }
    }
}
