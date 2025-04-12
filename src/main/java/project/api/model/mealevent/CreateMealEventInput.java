package project.api.model.mealevent;

import lombok.Data;

@Data
public class CreateMealEventInput {
    private String title;
    private String name;
    private String start;
    private String userId;
}