package project.api.repository;

import project.api.model.MealEvent;
import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface MealEventRepository extends ReactiveCosmosRepository<MealEvent, String> {

}