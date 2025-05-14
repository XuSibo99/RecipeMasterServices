package project.api.model.aisuggestion;

import java.util.List;

public record RecipeFull(
        Long id,
        String title,
        String image,
        Integer readyInMinutes,
        Integer servings,
        String summary,
        String sourceUrl,
        List<String> extendedIngredients) {
}
