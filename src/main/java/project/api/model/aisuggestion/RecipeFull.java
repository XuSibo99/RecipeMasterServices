package project.api.model.aisuggestion;

import java.util.List;

public record RecipeFull(
                Long id,
                String title,
                String image,
                String sourceUrl,
                String spoonacularSourceUrl,

                Integer healthScore,
                Double calories,
                List<String> dishTypes,
                List<String> cuisines,
                Integer readyInMinutes,
                Integer servings,
                Boolean vegetarian,
                Boolean vegan,
                Boolean glutenFree,
                Boolean dairyFree,

                String summary,
                String instructions,
                List<Instruction> analyzedInstructions,
                Nutrition nutrition) {
        public static record Instruction(
                        String step,
                        List<IngredientRef> ingredients,
                        List<EquipmentRef> equipment) {
        }

        public static record IngredientRef(
                        String id,
                        String name) {
        }

        public static record EquipmentRef(
                        String id,
                        String name) {
        }

        public static record Nutrition(
                        List<Nutrient> nutrients) {
        }

        public static record Nutrient(
                        String name,
                        Double amount,
                        String unit) {
        }
}