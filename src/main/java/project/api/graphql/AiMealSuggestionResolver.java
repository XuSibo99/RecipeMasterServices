package project.api.graphql;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import project.api.model.aisuggestion.DietaryRestriction;
import project.api.model.aisuggestion.RecipeBrief;
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
        // delegates straight through to your service, which returns a Mono<String>
        return aiSuggestionService.generateSuggestion(prompt, restrictions);
    }

    @QueryMapping
    public List<RecipeBrief> searchRecipes(
            @Argument String query,
            @Argument List<DietaryRestriction> restrictions,
            @Argument Integer number) {
        // delegates to your Spoonacular client
        return spoonacularClientService.searchRecipes(query, restrictions, number);
    }

    @QueryMapping
    public RecipeFull getRecipe(@Argument Long id) {
        return spoonacularClientService.getRecipe(id);
    }
}
