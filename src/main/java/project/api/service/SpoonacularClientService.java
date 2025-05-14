package project.api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import project.api.model.aisuggestion.DietaryRestriction;
import project.api.model.aisuggestion.RecipeBrief;
import project.api.model.aisuggestion.RecipeFull;
import reactor.core.publisher.Mono;

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

    public List<RecipeBrief> searchRecipes(
            String query,
            List<DietaryRestriction> restrictions,
            int number) {
        String dietParam = restrictions.stream()
                .map(DietaryRestriction::name)
                .map(String::toLowerCase)
                .collect(Collectors.joining(","));

        var response = webClient.get()
                .uri(uriBuilder -> {
                    var b = uriBuilder
                            .path("/recipes/complexSearch")
                            .queryParam("apiKey", spoonacularApiKey)
                            .queryParam("query", query)
                            .queryParam("number", number);
                    if (!dietParam.isEmpty()) {
                        b = b.queryParam("diet", dietParam);
                    }
                    return b.build();
                })
                .retrieve()
                .bodyToMono(ComplexSearchResponse.class)
                .block();

        return response.getResults().stream()
                .map(r -> new RecipeBrief(
                        r.getId(),
                        r.getTitle(),
                        r.getImage(),
                        r.getReadyInMinutes()))
                .collect(Collectors.toList());
    }

    public RecipeFull getRecipe(Long id) {
        var info = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/recipes/{id}/information")
                        .queryParam("apiKey", spoonacularApiKey)
                        .queryParam("includeNutrition", false)
                        .build(id))
                .retrieve()
                .bodyToMono(RecipeInfoResponse.class)
                .block();

        return new RecipeFull(
                info.getId(),
                info.getTitle(),
                info.getImage(),
                info.getReadyInMinutes(),
                info.getServings(),
                info.getSummary(),
                info.getSourceUrl(),
                info.getExtendedIngredients().stream()
                        .map(RecipeInfoResponse.Ingredient::getOriginalString)
                        .collect(Collectors.toList()));
    }

    // --- internal DTOs for deserialization ---

    public static class ComplexSearchResponse {
        private List<SearchResult> results;

        public ComplexSearchResponse() {
        }

        public List<SearchResult> getResults() {
            return results;
        }

        public void setResults(List<SearchResult> results) {
            this.results = results;
        }
    }

    public static class SearchResult {
        private Long id;
        private String title;
        private String image;
        private Integer readyInMinutes;

        public SearchResult() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Integer getReadyInMinutes() {
            return readyInMinutes;
        }

        public void setReadyInMinutes(Integer readyInMinutes) {
            this.readyInMinutes = readyInMinutes;
        }
    }

    public static class RecipeInfoResponse {
        private Long id;
        private String title;
        private String image;
        private String summary;
        private String sourceUrl;
        private Integer readyInMinutes;
        private Integer servings;
        private List<Ingredient> extendedIngredients;

        public RecipeInfoResponse() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }

        public Integer getReadyInMinutes() {
            return readyInMinutes;
        }

        public void setReadyInMinutes(Integer readyInMinutes) {
            this.readyInMinutes = readyInMinutes;
        }

        public Integer getServings() {
            return servings;
        }

        public void setServings(Integer servings) {
            this.servings = servings;
        }

        public List<Ingredient> getExtendedIngredients() {
            return extendedIngredients;
        }

        public void setExtendedIngredients(List<Ingredient> extendedIngredients) {
            this.extendedIngredients = extendedIngredients;
        }

        public static class Ingredient {
            private String originalString;

            public Ingredient() {
            }

            public String getOriginalString() {
                return originalString;
            }

            public void setOriginalString(String originalString) {
                this.originalString = originalString;
            }
        }
    }
}
