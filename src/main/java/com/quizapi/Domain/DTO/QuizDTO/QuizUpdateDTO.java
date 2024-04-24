package com.quizapi.Domain.DTO.QuizDTO;

public record QuizUpdateDTO(
        String name,
        String description) {
    public String getName( ) {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
