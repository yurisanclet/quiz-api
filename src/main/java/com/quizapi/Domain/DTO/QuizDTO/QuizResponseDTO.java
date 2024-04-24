package com.quizapi.Domain.DTO.QuizDTO;

import com.quizapi.Domain.Entity.Question;
import com.quizapi.Domain.Entity.Quiz;

import java.util.List;

public record QuizResponseDTO(
        Long id,
        String name,
        String description,
        List<Question> questions
) {

}
