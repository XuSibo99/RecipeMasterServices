package project.api.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.api.model.MealEvent;
import project.api.repository.MealEventRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/test-cosmos")
public class MealEventController {

    private final MealEventRepository mealEventRepository;

    public MealEventController(MealEventRepository mealEventRepository) {
        this.mealEventRepository = mealEventRepository;
    }

    // GET /test-cosmos/all -> returns all events in container
    @GetMapping("/all")
    public Flux<MealEvent> getAllEvents() {
        return mealEventRepository.findAll();
    }

    // POST /test-cosmos/add -> create new meal event
    @PostMapping("/add")
    public Mono<MealEvent> addMealEvent(@RequestBody MealEvent event) {
        if (event.getId() == null) {
            event.setId(UUID.randomUUID().toString());
        }
        return mealEventRepository.save(event);
    }

    // GET /test-cosmos/hardcoded -> inserts a single event
    @GetMapping("/hardcoded")
    public String insertHardcoded() {
        MealEvent e = new MealEvent("Dinner", "Pizza", "2025-01-07T19:00:00", "testUser");
        e.setId(UUID.randomUUID().toString());
        mealEventRepository.save(e);
        return "Inserted a test event with ID: " + e.getId();
    }
}
