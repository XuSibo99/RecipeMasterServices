package project.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.api.model.MealEvent;
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

    public Flux<MealEvent> getMealEventByUserId(String userId) {
        return mealEventRepository.findByUserId(userId);
    }

    public Mono<MealEvent> createMealEvent(MealEvent mealEvent) {
        return mealEventRepository.save(mealEvent);
    }

    public Mono<MealEvent> updateMealEvent(String id, String title, String name, String start, String userId) {
        return mealEventRepository.findById(id)
                .flatMap(existingMealEvent -> {
                    existingMealEvent.setTitle(title != null ? title : existingMealEvent.getTitle());
                    existingMealEvent.setName(name != null ? name : existingMealEvent.getName());
                    existingMealEvent.setStart(start != null ? start : existingMealEvent.getStart());
                    existingMealEvent.setUserId(userId != null ? userId : existingMealEvent.getUserId());

                    return mealEventRepository.save(existingMealEvent);
                });
    }

    public Mono<String> deleteMealEvent(String id) {
        return mealEventRepository.findById(id)
                .flatMap(existingMealEvent -> mealEventRepository.delete(existingMealEvent)
                        .thenReturn("Deleted MealEvent with id: " + id));
    }
}
