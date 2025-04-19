package project.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Service;

import project.api.model.mealevent.MealEvent;
import project.api.model.mealevent.UpdateMealEventInput;
import project.api.repository.MealEventRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MealEventService {

    @Autowired
    private MealEventRepository mealEventRepository;

    public Mono<MealEvent> getMealEventById(String id) {
        return mealEventRepository.findById(id);
    }

    public Flux<MealEvent> getMealEventsByUserId(String userId) {
        return mealEventRepository.findByUserId(userId);
    }

    public Mono<MealEvent> createMealEvent(MealEvent mealEvent) {
        return mealEventRepository.save(mealEvent);
    }

    @MutationMapping
    public Mono<MealEvent> updateMealEvent(@Argument String id, @Argument UpdateMealEventInput input) {
        return mealEventRepository.findById(id)
                .flatMap(existing -> {
                    if (input.getTitle() != null)
                        existing.setTitle(input.getTitle());
                    if (input.getName() != null)
                        existing.setName(input.getName());
                    if (input.getStart() != null)
                        existing.setStart(input.getStart());
                    if (input.getUserId() != null)
                        existing.setUserId(input.getUserId());
                    return mealEventRepository.save(existing);
                });
    }

    public Mono<String> deleteMealEvent(String id) {
        return mealEventRepository.findById(id)
                .flatMap(existingMealEvent -> mealEventRepository.delete(existingMealEvent)
                        .thenReturn("Deleted MealEvent with id: " + id));
    }
}
