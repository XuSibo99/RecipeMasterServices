package project.api.model.aisuggestion;

public record RecipeSummary(
                Long id,
                String title,
                String image,
                Integer readyInMinutes,
                Integer servings,
                String sourceUrl,
                String spoonacularSourceUrl,
                Double calories) {
}
