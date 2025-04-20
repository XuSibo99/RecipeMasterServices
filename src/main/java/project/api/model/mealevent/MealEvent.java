package project.api.model.mealevent;

import org.springframework.data.annotation.Id;

import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;

import lombok.Data;

import com.azure.spring.data.cosmos.core.mapping.Container;

@Data
@Container(containerName = "recipemaster")
public class MealEvent {

    @Id
    @GeneratedValue
    private String id;

    // event title
    private String title;

    // meal name
    private String name;

    // meal time
    private String start;

    // Partition key used by Cosmos for scaling and queries
    @PartitionKey
    private String userId;

    // how often the event recurrent
    private String recurrence;

    // Constructors
    public MealEvent() {
    }

    public MealEvent(String title, String name, String start, String userId) {
        this.title = title;
        this.name = name;
        this.start = start;
        this.userId = userId;
    }

    public MealEvent(String title, String name, String start, String userId, String recurrence) {
        this.title = title;
        this.name = name;
        this.start = start;
        this.userId = userId;
        this.recurrence = recurrence;
    }

    // Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }
}
