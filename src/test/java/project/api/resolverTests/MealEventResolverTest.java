package project.api.resolverTests;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import project.api.graphql.MealEventResolver;
import project.api.model.mealevent.MealEvent;
import project.api.service.MealEventService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@GraphQlTest(MealEventResolver.class)
public class MealEventResolverTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private MealEventService mealEventService;

    @Test
    void getMealEventById_returnsMeal() {

        MealEvent mockMeal = new MealEvent("Lunch", "Pasta", "2025-05-01", "sibo.xu");
        mockMeal.setId("meal-123");

        Mockito.when(mealEventService.getMealEventById("meal-123")).thenReturn(Mono.just(mockMeal));

        graphQlTester.documentName("getMealEventById").variable("id", "meal-123").execute()
                .path("getMealEventById.name").entity(String.class).isEqualTo("Pasta");
    }

    @Test
    void getMealEventById_returnsNullIfNotFound() {
        Mockito.when(mealEventService.getMealEventById("unknown-id")).thenReturn(Mono.empty());

        graphQlTester.documentName("getMealEventById")
                .variable("id", "unknown-id")
                .execute()
                .path("getMealEventById").valueIsNull();
    }

    @Test
    void createMealEvent_returnsCreatedMeal() {
        MealEvent inputMeal = new MealEvent("Dinner", "Steak", "2025-06-01", "sibo.xu");
        inputMeal.setId("meal-456");

        Mockito.when(mealEventService.createMealEvent(Mockito.any()))
                .thenReturn(Mono.just(inputMeal));

        Map<String, Object> input = new HashMap<>();
        input.put("title", "Dinner");
        input.put("name", "Steak");
        input.put("start", "2025-06-01");
        input.put("userId", "sibo.xu");

        graphQlTester.documentName("createMealEvent")
                .variable("input", input)
                .execute()
                .path("createMealEvent.name").entity(String.class).isEqualTo("Steak");
    }

    @Test
    void getMealEventsByUserId_returnsMealList() {
        MealEvent meal = new MealEvent("Lunch", "Sandwich", "2025-06-02", "sibo.xu");
        meal.setId("meal-789");

        Mockito.when(mealEventService.getMealEventsByUserId("sibo.xu"))
                .thenReturn(Flux.just(meal));

        graphQlTester.documentName("getMealEventsByUserId")
                .variable("userId", "sibo.xu")
                .execute()
                .path("getMealEventsByUserId[0].name").entity(String.class).isEqualTo("Sandwich");
    }

    @Test
    void updateMealEvent_returnsUpdatedMeal() {
        MealEvent updatedMeal = new MealEvent("Lunch", "Sushi Bowl", "2025-06-01", "sibo.xu");
        updatedMeal.setId("meal-123");

        Mockito.when(mealEventService.updateMealEvent(Mockito.eq("meal-123"), Mockito.any()))
                .thenReturn(Mono.just(updatedMeal));

        Map<String, Object> input = new HashMap<>();
        input.put("title", "Lunch");
        input.put("name", "Sushi Bowl");
        input.put("start", "2025-06-01");
        input.put("userId", "sibo.xu");

        graphQlTester.documentName("updateMealEvent")
                .variable("id", "meal-123")
                .variable("input", input)
                .execute()
                .path("updateMealEvent.name").entity(String.class).isEqualTo("Sushi Bowl");
    }

    @Test
    void updateMealEvent_returnsNullIfNotFound() {
        Mockito.when(mealEventService.updateMealEvent(Mockito.eq("nonexistent-id"), Mockito.any()))
                .thenReturn(Mono.empty());

        graphQlTester.documentName("updateMealEvent")
                .variable("id", "nonexistent-id")
                .variable("input", Map.of(
                        "title", "Lunch",
                        "name", "Sushi Bowl",
                        "start", "2025-06-01",
                        "userId", "sibo.xu"))
                .execute()
                .path("updateMealEvent").valueIsNull();
    }

    @Test
    void deleteMealEvent_returnsConfirmation() {
        Mockito.when(mealEventService.deleteMealEvent("meal-123"))
                .thenReturn(Mono.just("meal-123"));

        graphQlTester.documentName("deleteMealEvent")
                .variable("id", "meal-123")
                .execute()
                .path("deleteMealEvent").entity(String.class).isEqualTo("meal-123");
    }

    @Test
    void deleteMealEvent_invalidId_returnsNull() {
        Mockito.when(mealEventService.deleteMealEvent("nonexistent-id"))
                .thenReturn(Mono.empty());

        graphQlTester.documentName("deleteMealEvent")
                .variable("id", "nonexistent-id")
                .execute()
                .path("deleteMealEvent").valueIsNull();
    }
}
