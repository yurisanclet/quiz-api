package com.quizapi.Controller;

import com.quizapi.Domain.DTO.QuizDTO.QuizResponseDTO;
import com.quizapi.Domain.DTO.QuizDTO.QuizUpdateDTO;
import com.quizapi.Domain.Entity.Answer;
import com.quizapi.Domain.Entity.Question;
import com.quizapi.Domain.Entity.Quiz;
import com.quizapi.Services.Quiz.QuizService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public ResponseEntity<String> createQuiz(@RequestBody Quiz quiz) {
        try {
            quizService.createQuiz(quiz);
            return ResponseEntity.ok("Quiz criado com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<QuizResponseDTO>> getAllQuizzes() {
        List<QuizResponseDTO> quizzes = quizService.getAllQuizzes();
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        Quiz quiz = quizService.getQuizById(id);
        return ResponseEntity.ok(quiz);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long id, @RequestBody QuizUpdateDTO quiz) {
        Quiz updatedQuiz = quizService.updateQuiz(id, quiz);
        return ResponseEntity.ok(updatedQuiz);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Quiz> patchQuiz(@PathVariable Long id, @RequestBody QuizUpdateDTO quiz) {
        Quiz patchedQuiz = quizService.patchQuiz(id, quiz);
        return ResponseEntity.ok(patchedQuiz);
    }

    @PostMapping("/quiz/{quizId}/answer")
    public ResponseEntity<Quiz> addAnswerToQuiz(@PathVariable Long quizId, @RequestBody Answer answer) {
        Quiz quiz = quizService.getQuizById(quizId);
        for (Question question : quiz.getQuestions()) {
            quizService.addAnswerToQuestion(question.getId(), answer);
        }
        return ResponseEntity.ok(quiz);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuiz(@PathVariable Long id) {
        try {
            quizService.deleteQuiz(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}