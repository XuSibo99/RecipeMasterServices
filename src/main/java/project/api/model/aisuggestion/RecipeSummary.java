package project.api.model.aisuggestion;

public record RecipeSummary(
        Long id,
        String title,
        String image,
        String sourceUrl,
        String spoonacularSourceUrl) {
}