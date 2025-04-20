package project.api.resolverTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

                MealEvent mockMeal = new MealEvent("Lunch", "Pasta", "2025-05-01", "sibo.xu", "WEEKLY");
                mockMeal.setId("meal-123");

                Mockito.when(mealEventService.getMealEventById("meal-123")).thenReturn(Mono.just(mockMeal));

                graphQlTester.documentName("getMealEventById").variable("id", "meal-123").execute()
                                .path("getMealEventById.title").entity(String.class).isEqualTo("Lunch")
                                .path("getMealEventById.name").entity(String.class).isEqualTo("Pasta")
                                .path("getMealEventById.start").entity(String.class).isEqualTo("2025-05-01")
                                .path("getMealEventById.userId").entity(String.class).isEqualTo("sibo.xu")
                                .path("getMealEventById.recurrence").entity(String.class).isEqualTo("WEEKLY");
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
                MealEvent inputMeal = new MealEvent("Dinner", "Steak", "2025-06-01", "sibo.xu", "WEEKLY");
                inputMeal.setId("meal-456");
                Mockito.when(mealEventService.createMealEvent(Mockito.any()))
                                .thenReturn(Mono.just(inputMeal));

                Map<String, Object> input = new HashMap<>();
                input.put("title", "Dinner");
                input.put("name", "Steak");
                input.put("start", "2025-06-01");
                input.put("userId", "sibo.xu");
                input.put("recurrence", "WEEKLY");

                graphQlTester.documentName("createMealEvent")
                                .variable("id", "meal-123")
                                .variable("input", input)
                                .execute()
                                .path("createMealEvent").entity(MealEvent.class)
                                .satisfies(meal -> {
                                        assertEquals("Dinner", meal.getTitle());
                                        assertEquals("Steak", meal.getName());
                                        assertEquals("2025-06-01", meal.getStart());
                                        assertEquals("sibo.xu", meal.getUserId());
                                        assertEquals("WEEKLY", meal.getRecurrence());
                                });
        }

        @Test
        void getMealEventsByUserId_returnsMealList() {
                MealEvent meal = new MealEvent("Dinner", "Steak", "2025-06-01", "sibo.xu", "WEEKLY");
                meal.setId("meal-789");

                Mockito.when(mealEventService.getMealEventsByUserId("sibo.xu"))
                                .thenReturn(Flux.just(meal));

                graphQlTester.documentName("getMealEventsByUserId")
                                .variable("userId", "sibo.xu")
                                .execute()
                                .path("getMealEventsByUserId").entityList(MealEvent.class)
                                .satisfies(meals -> {
                                        assertEquals(1, meals.size());
                                        MealEvent result = meals.get(0);
                                        assertEquals("Dinner", result.getTitle());
                                        assertEquals("Steak", result.getName());
                                        assertEquals("2025-06-01", result.getStart());
                                        assertEquals("sibo.xu", result.getUserId());
                                        assertEquals("WEEKLY", result.getRecurrence());
                                });
        }

        @Test
        void updateMealEvent_returnsUpdatedMeal() {
                MealEvent updatedMeal = new MealEvent("Lunch", "Sushi Bowl", "2025-06-01", "sibo.xu", "DAILY");
                updatedMeal.setId("meal-123");

                Mockito.when(mealEventService.updateMealEvent(Mockito.eq("meal-123"), Mockito.any()))
                                .thenReturn(Mono.just(updatedMeal));

                Map<String, Object> input = new HashMap<>();
                input.put("title", "Lunch");
                input.put("name", "Sushi Bowl");
                input.put("start", "2025-06-01");
                input.put("userId", "sibo.xu");
                input.put("recurrence", "DAILY");

                graphQlTester.documentName("updateMealEvent")
                                .variable("id", "meal-123")
                                .variable("input", input)
                                .execute()
                                .path("updateMealEvent").entity(MealEvent.class)
                                .satisfies(meal -> {
                                        assertEquals("Lunch", meal.getTitle());
                                        assertEquals("Sushi Bowl", meal.getName());
                                        assertEquals("2025-06-01", meal.getStart());
                                        assertEquals("sibo.xu", meal.getUserId());
                                        assertEquals("DAILY", meal.getRecurrence());
                                });
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
                                                "userId", "sibo.xu",
                                                "recurrence", "WEEKLY"))
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
