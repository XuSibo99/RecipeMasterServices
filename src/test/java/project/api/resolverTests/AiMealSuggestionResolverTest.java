package project.api.resolverTests;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import project.api.graphql.AiMealSuggestionResolver;
import project.api.service.AiSuggestionService;
import reactor.core.publisher.Mono;

@GraphQlTest(AiMealSuggestionResolver.class)
public class AiMealSuggestionResolverTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private AiSuggestionService aiSuggestionService;

    @Test
    void generateAiMealSuggestion_returnsResponse() {
        Mockito.when(aiSuggestionService.generateSuggestion("give me 3 lunch ideas"))
                .thenReturn(Mono.just("1. Sandwich\n2. Pasta\n3. Salad"));

        graphQlTester.documentName("generateAiMealSuggestion")
                .variable("prompt", "give me 3 lunch ideas")
                .execute()
                .path("generateAiMealSuggestion")
                .entity(String.class)
                .isEqualTo("1. Sandwich\n2. Pasta\n3. Salad");
    }
}
