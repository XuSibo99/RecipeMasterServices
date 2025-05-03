package project.api.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import project.api.service.AiSuggestionService;
import reactor.core.publisher.Mono;

@Controller
public class AiMealSuggestionResolver {

    @Autowired
    private AiSuggestionService aiSuggestionService;

    @MutationMapping
    public Mono<String> generateAiMealSuggestion(@Argument String prompt) {
        return aiSuggestionService.generateSuggestion(prompt);
    }
}
