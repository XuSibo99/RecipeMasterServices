package project.api.service;

import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import project.api.model.aisuggestion.AiResponse;
import reactor.core.publisher.Mono;

@Service
public class AiSuggestionService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public Mono<String> generateSuggestion(String prompt) {
        WebClient client = WebClient.builder()
                .baseUrl(OPENAI_URL)
                .defaultHeader("Authorization", "Bearer " + openAiApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();

        Map<String, Object> body = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)));

        return client.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(AiResponse.class)
                .map(res -> res.getChoices().get(0).getMessage().getContent());
    }
}
