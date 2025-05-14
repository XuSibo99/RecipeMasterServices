package project.api.model.aisuggestion;

public record RecipeBrief(
        Long id,
        String title,
        String image,
        Integer readyInMinutes) {

}
