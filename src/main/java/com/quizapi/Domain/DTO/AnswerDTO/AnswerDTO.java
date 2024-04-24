package com.quizapi.Domain.DTO.AnswerDTO;

public record AnswerDTO(
        Long id,
        String answerDescription,
        Long questionId
) {
}