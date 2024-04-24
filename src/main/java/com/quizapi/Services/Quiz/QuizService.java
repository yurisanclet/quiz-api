package com.quizapi.Services.Quiz;

import com.quizapi.Domain.DTO.AnswerDTO.AnswerDTO;
import com.quizapi.Domain.DTO.QuizDTO.QuizResponseDTO;
import com.quizapi.Domain.DTO.QuizDTO.QuizUpdateDTO;
import com.quizapi.Domain.Entity.Answer;
import com.quizapi.Domain.Entity.Question;
import com.quizapi.Domain.Entity.Quiz;
import com.quizapi.Repository.AnswerRepository;
import com.quizapi.Repository.QuestionRepository;
import com.quizapi.Repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        // Passo 1: Salve o quiz no repositório de quiz
        Quiz savedQuiz = quizRepository.save(quiz);
        for(Question question : quiz.getQuestions()){
            question.setQuiz(savedQuiz);
            questionRepository.save(question);
        }
        return savedQuiz;
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

    public void addAnswerToQuestion(Long questionId, Answer answer) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Questão não encontrada"));

        answer.setQuestionId(question);
        answerRepository.save(answer);
    }

    public List<AnswerDTO> getAllAnswersFromQuiz(Long quizId) {
        Quiz quiz = getQuizById(quizId);
        return quiz.getQuestions().stream()
                .map(Question::getAnswer)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private AnswerDTO toDTO(Answer answer) {
        return new AnswerDTO(
                answer.getId(),
                answer.getAnswerDescription(),
                answer.getQuestionId().getId()
        );
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