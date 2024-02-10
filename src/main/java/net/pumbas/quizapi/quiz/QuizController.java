package net.pumbas.quizapi.quiz;

import jakarta.validation.Valid;
import java.util.List;
import net.pumbas.quizapi.config.Constants;
import net.pumbas.quizapi.question.CreateQuestionDto;
import net.pumbas.quizapi.question.QuestionDto;
import net.pumbas.quizapi.question.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API_V1 + "/quizzes")
public class QuizController {

  private final QuizService quizService;
  private final QuestionService questionService;

  public QuizController(QuizService quizService, QuestionService questionService) {
    this.quizService = quizService;
    this.questionService = questionService;
  }

  @GetMapping
  public List<QuizSummaryDto> getQuizzes() {
    return this.quizService.getQuizzes();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public QuizDto createQuiz(@Valid @RequestBody CreateQuizDto createQuizDto) {
    return this.quizService.createQuiz(createQuizDto);
  }

  @GetMapping("/{quizId}")
  public QuizDto getQuiz(@PathVariable Long quizId) {
    return this.quizService.getQuizDto(quizId);
  }

  @PatchMapping("/{quizId}")
  public QuizDto updateQuiz(
      @PathVariable Long quizId,
      @Valid @RequestBody UpdateQuizDto updateQuizDto
  ) {
    return this.quizService.updateQuiz(quizId, updateQuizDto);
  }

  @DeleteMapping("/{quizId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteQuiz(@PathVariable Long quizId) {
    this.quizService.deleteQuiz(quizId);
  }

  @PostMapping("/{quizId}/questions")
  @ResponseStatus(HttpStatus.CREATED)
  public QuestionDto createQuestion(
      @PathVariable Long quizId,
      @Valid @RequestBody CreateQuestionDto createQuestionDto
  ) {
    return this.questionService.createQuestion(quizId, createQuestionDto);
  }

  @PutMapping("/{quizId}/questions/{questionId}")
  public QuestionDto updateQuestion(
      @PathVariable Long quizId,
      @PathVariable Long questionId,
      @Valid @RequestBody CreateQuestionDto createQuestionDto
  ) {
    return this.questionService.updateQuestion(quizId, questionId, createQuestionDto);
  }

  @DeleteMapping("/{quizId}/questions/{questionId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteQuestion(
      @PathVariable Long quizId,
      @PathVariable Long questionId
  ) {
    this.questionService.deleteQuestion(quizId, questionId);
  }
}
