package project.api.service;

import java.util.List;
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

        return response.getResults().stream()
                .map(r -> {
                    double calories = r.getNutrition().getNutrients().stream()
                            .filter(n -> "Calories".equalsIgnoreCase(n.getName()))
                            .findFirst()
                            .map(Nutrient::getAmount)
                            .orElse(0.0);

                    return new RecipeSummary(
                            r.getId(),
                            r.getTitle(),
                            r.getImage(),
                            r.getReadyInMinutes(),
                            r.getServings(),
                            r.getSourceUrl(),
                            r.getSpoonacularSourceUrl(),
                            calories);
                })
                .collect(Collectors.toList());
    }

    public RecipeFull getRecipe(Long id) {
        RecipeInfoResponse info = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/recipes/{id}/information")
                        .queryParam("apiKey", spoonacularApiKey)
                        .queryParam("includeNutrition", false)
                        .build(id))
                .retrieve()
                .bodyToMono(RecipeInfoResponse.class)
                .block();

        if (info == null) {
            throw new RuntimeException("Failed to fetch recipe info for id " + id);
        }

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

    public static class ComplexSearchResponse {
        private List<SearchResult> results;

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
        private Integer servings;
        private String sourceUrl;
        private Nutrition nutrition;
        private String spoonacularSourceUrl;

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

        public Integer getServings() {
            return servings;
        }

        public void setServings(Integer servings) {
            this.servings = servings;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }

        public Nutrition getNutrition() {
            return nutrition;
        }

        public void setNutrition(Nutrition nutrition) {
            this.nutrition = nutrition;
        }

        public String getSpoonacularSourceUrl() {
            return spoonacularSourceUrl;
        }

        public void setSpoonacularSourceUrl(String u) {
            this.spoonacularSourceUrl = u;
        }
    }

    public static class Nutrition {
        private List<Nutrient> nutrients;

        public List<Nutrient> getNutrients() {
            return nutrients;
        }

        public void setNutrients(List<Nutrient> nutrients) {
            this.nutrients = nutrients;
        }
    }

    public static class Nutrient {
        private String name;
        private Double amount;
        private String unit;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
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

            public String getOriginalString() {
                return originalString;
            }

            public void setOriginalString(String originalString) {
                this.originalString = originalString;
            }
        }
    }
}
