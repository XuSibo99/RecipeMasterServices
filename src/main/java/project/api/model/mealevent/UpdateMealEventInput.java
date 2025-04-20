package project.api.model.mealevent;

import lombok.Data;

@Data
public class UpdateMealEventInput {
    private String title;
    private String name;
    private String start;
    private String userId;
    private String recurrence;
}