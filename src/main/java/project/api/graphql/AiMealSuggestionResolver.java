package project.api.graphql;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import project.api.model.aisuggestion.DietaryRestriction;
import project.api.model.aisuggestion.RecipeSummary;
import project.api.model.aisuggestion.RecipeFull;
import project.api.service.AiSuggestionService;
import project.api.service.SpoonacularClientService;
import reactor.core.publisher.Mono;

@Controller
public class AiMealSuggestionResolver {

    private final AiSuggestionService aiSuggestionService;
    private final SpoonacularClientService spoonacularClientService;

    public AiMealSuggestionResolver(
            AiSuggestionService aiSuggestionService,
            SpoonacularClientService spoonacularClientService) {
        this.aiSuggestionService = aiSuggestionService;
        this.spoonacularClientService = spoonacularClientService;
    }

    @MutationMapping
    public Mono<String> generateAiMealSuggestion(
            @Argument String prompt,
            @Argument List<DietaryRestriction> restrictions) {
        return aiSuggestionService.generateSuggestion(prompt, restrictions);
    }

    @QueryMapping
    public List<RecipeSummary> searchRecipes(
            @Argument String query,
            @Argument List<DietaryRestriction> restrictions,
            @Argument Integer number) {
        return spoonacularClientService.searchRecipes(query, restrictions, number);
    }

    @QueryMapping
    public RecipeFull getRecipe(@Argument Long id) {
        return spoonacularClientService.getRecipe(id);
    }

    @QueryMapping
    public List<RecipeSummary> searchRecipesByTitles(
            @Argument List<String> titles,
            @Argument List<DietaryRestriction> restrictions) {
        return titles.stream()
                .flatMap(title -> spoonacularClientService
                        .searchRecipes(title, restrictions, 1)
                        .stream()
                        .limit(1))
                .collect(Collectors.toList());
    }
}
