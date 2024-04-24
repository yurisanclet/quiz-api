package com.quizapi.Services.Quiz;

import com.quizapi.Domain.Entity.Quiz;

import java.util.List;

public interface IQuizService {
    void createQuiz(Quiz quiz);

    List<Quiz> getAllQuizzes();

    Quiz getQuizById(Long id);

    Quiz updateQuiz(Long id, Quiz quiz);

    void deleteQuiz(Long id);
}
