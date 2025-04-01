package project.api.repository;

import project.api.model.MealEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface MealEventRepository extends ReactiveCosmosRepository<MealEvent, String> {

    Mono<MealEvent> findById(String id);

    Flux<MealEvent> findByUserId(String userId);

}