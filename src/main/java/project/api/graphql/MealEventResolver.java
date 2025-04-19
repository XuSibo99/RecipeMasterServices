package project.api.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import project.api.model.mealevent.MealEvent;
import project.api.model.mealevent.UpdateMealEventInput;
import project.api.model.mealevent.CreateMealEventInput;
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
    public Flux<MealEvent> getMealEventsByUserId(@Argument String userId) {
        return mealEventService.getMealEventsByUserId(userId);
    }

    @MutationMapping
    public Mono<MealEvent> createMealEvent(@Argument CreateMealEventInput input) {
        MealEvent newMealEvent = new MealEvent(
                input.getTitle(),
                input.getName(),
                input.getStart(),
                input.getUserId());
        return mealEventService.createMealEvent(newMealEvent);
    }

    @MutationMapping
    public Mono<MealEvent> updateMealEvent(@Argument String id, @Argument UpdateMealEventInput input) {
        return mealEventService.updateMealEvent(id, input);
    }

    @MutationMapping
    public Mono<String> deleteMealEvent(@Argument String id) {
        return mealEventService.deleteMealEvent(id);
    }
}
