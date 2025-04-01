package project.api.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import project.api.model.MealEvent;
import project.api.service.MealEventService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class MealEventResolver {

    @Autowired
    private MealEventService mealEventService;

    @QueryMapping
    public Mono<MealEvent> getMealEventById(@Argument String id) {
        return mealEventService.getMealEventById(id);
    }

    @QueryMapping
    public Flux<MealEvent> getMealEventByUserId(@Argument String userId) {
        return mealEventService.getMealEventByUserId(userId);
    }

    @MutationMapping
    public Mono<MealEvent> createMealEvent(@Argument String title, @Argument String name,
            @Argument String start, @Argument String userId) {
        MealEvent newMealEvent = new MealEvent(title, name, start, userId);
        return mealEventService.createMealEvent(newMealEvent);
    }

    @MutationMapping
    public Mono<MealEvent> updateMealEvent(@Argument String id, @Argument String title,
            @Argument String name, @Argument String start,
            @Argument String userId) {
        return mealEventService.updateMealEvent(id, title, name, start, userId);
    }

    @MutationMapping
    public Mono<String> deleteMealEvent(@Argument String id) {
        return mealEventService.deleteMealEvent(id);
    }
}
