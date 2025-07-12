package org.example.ktigerstudybe.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CreateChatConversationRequest {
    @NotNull(message = "User ID không được để trống")
    private Long userId;

    @NotBlank(message = "Scenario không được để trống")
    @Pattern(regexp = "^(restaurant|shopping|direction|introduction|daily)$",
            message = "Scenario phải là một trong: restaurant, shopping, direction, introduction, daily")
    private String scenario;

    @NotBlank(message = "Difficulty không được để trống")
    @Pattern(regexp = "^(beginner|intermediate|advanced)$",
            message = "Difficulty phải là một trong: beginner, intermediate, advanced")
    private String difficulty;

    // Constructors
    public CreateChatConversationRequest() {}

    public CreateChatConversationRequest(Long userId, String scenario, String difficulty) {
        this.userId = userId;
        this.scenario = scenario;
        this.difficulty = difficulty;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getScenario() { return scenario; }
    public void setScenario(String scenario) { this.scenario = scenario; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
}