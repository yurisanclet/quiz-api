package com.quizapi.Services.Quiz;

import com.quizapi.Domain.DTO.QuizDTO.QuizResponseDTO;
import com.quizapi.Domain.DTO.QuizDTO.QuizUpdateDTO;
import com.quizapi.Domain.DTO.UserDTO.UserResponseDTO;
import com.quizapi.Domain.Entity.Answer;
import com.quizapi.Domain.Entity.Question;
import com.quizapi.Domain.Entity.Quiz;
import com.quizapi.Domain.Entity.User;
import com.quizapi.Repository.AnswerRepository;
import com.quizapi.Repository.QuestionRepository;
import com.quizapi.Repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public QuizService(QuizRepository quizRepository, AnswerRepository answerRepository, QuestionRepository questionRepository) {

        this.quizRepository = quizRepository;
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    public Quiz createQuiz(Quiz quiz) {
        if (Objects.isNull(quiz) || Objects.isNull(quiz.getName()) || Objects.isNull(quiz.getDescription())) {
            throw new IllegalArgumentException("Quiz inválido");
        }
        if (quiz.getQuestions() != null) {
            quiz.getQuestions().forEach(question -> {
                if (Objects.isNull(question.getDescription())) {
                    throw new IllegalArgumentException("Pergunta inválida");
                }
                question.setQuiz(quiz);
                if (question.getAnswer() != null) {
                    throw new IllegalArgumentException("Não é permitido criar respostas neste momento");
                }
            });
        }
        return quizRepository.save(quiz);
    }

    public List<QuizResponseDTO> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();

        return quizzes.stream()
                .map(quiz -> new QuizResponseDTO(
                        quiz.getId(),
                        quiz.getName(),
                        quiz.getDescription(),
                        quiz.getQuestions()
                )).toList();
    }

    public Quiz getQuizById(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }
        return quizRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Quiz não encontrado"));
    }

    public Quiz updateQuiz(Long id, QuizUpdateDTO quiz) {
        if (Objects.isNull(id) || id <= 0 || Objects.isNull(quiz) || Objects.isNull(quiz.getName()) || Objects.isNull(quiz.getDescription())) {
            throw new IllegalArgumentException("Parâmetros inválidos");
        }
        Quiz quizToUpdate = getQuizById(id);
        quizToUpdate.setName(quiz.getName());
        quizToUpdate.setDescription(quiz.getDescription());
        return quizRepository.save(quizToUpdate);
    }

    public Quiz patchQuiz(Long id, QuizUpdateDTO quiz) {
        if (Objects.isNull(id) || id <= 0 || Objects.isNull(quiz)) {
            throw new IllegalArgumentException("Parâmetros inválidos");
        }
        Quiz quizToUpdate = getQuizById(id);
        if (Objects.nonNull(quiz.getName())) {
            quizToUpdate.setName(quiz.getName());
        }
        if (Objects.nonNull(quiz.getDescription())) {
            quizToUpdate.setDescription(quiz.getDescription());
        }
        return quizRepository.save(quizToUpdate);
    }

    public Question addAnswerToQuestion(Long questionId, Answer answer) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Questão não encontrada"));

        answer.setQuestion(question);
        answerRepository.save(answer);
        return question;
    }

    public void deleteQuiz(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }
        if (!quizRepository.existsById(id)) {
            throw new RuntimeException("Quiz não encontrado");
        }
        quizRepository.deleteById(id);
    }
}