package project.api.service;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import project.api.model.aisuggestion.AiResponse;
import project.api.model.aisuggestion.DietaryRestriction;
import reactor.core.publisher.Mono;

@Service
public class AiSuggestionService {

        @Value("${openai.api.key}")
        private String openAiApiKey;

        private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

        public Mono<String> generateSuggestion(String prompt, List<DietaryRestriction> restrictions) {
                WebClient client = WebClient.builder()
                                .baseUrl(OPENAI_URL)
                                .defaultHeader("Authorization", "Bearer " + openAiApiKey)
                                .defaultHeader("Content-Type", "application/json")
                                .build();

                String prefText = restrictions.stream()
                                .map(r -> r.name().toLowerCase().replace('_', ' '))
                                .collect(Collectors.joining(", "));
                String systemMsg = "The user’s dietary restrictions are: " + prefText + ".";

                Map<String, Object> body = Map.of(
                                "model", "gpt-3.5-turbo",
                                "messages", List.of(
                                                Map.of("role", "system", "content", systemMsg),
                                                Map.of("role", "user", "content", prompt)));

                return client.post()
                                .bodyValue(body)
                                .retrieve()
                                .bodyToMono(AiResponse.class)
                                .map(res -> res.getChoices().get(0).getMessage().getContent());
        }
}
