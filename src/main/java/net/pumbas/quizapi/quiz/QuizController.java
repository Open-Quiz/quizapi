package net.pumbas.quizapi.quiz;

import jakarta.validation.Valid;
import java.util.List;
import net.pumbas.quizapi.config.Constants;
import net.pumbas.quizapi.middleware.AuthenticationMiddleware.AuthContext;
import net.pumbas.quizapi.middleware.annotations.Secured;
import net.pumbas.quizapi.question.CreateQuestionDto;
import net.pumbas.quizapi.question.QuestionDto;
import net.pumbas.quizapi.question.QuestionService;
import net.pumbas.quizapi.user.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

@Secured
@RestController
@RequestMapping(Constants.API_V1 + "/quizzes")
public class QuizController {

  private final QuizService quizService;
  private final QuestionService questionService;
  private final Log logger = LogFactory.getLog(QuizController.class);

  public QuizController(QuizService quizService, QuestionService questionService) {
    this.quizService = quizService;
    this.questionService = questionService;
  }

  @GetMapping
  public List<QuizSummaryDto> getQuizzes() {
    User requester = AuthContext.getCurrentUser();
    this.logger.info("Get quizzes for '%s'".formatted(requester.getUsername()));
    return this.quizService.getQuizzes(requester);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public QuizDto createQuiz(@Valid @RequestBody CreateQuizDto createQuizDto) {
    User requester = AuthContext.getCurrentUser();
    this.logger.info(
        "Create quiz: %s for '%s'".formatted(createQuizDto, requester.getUsername()));

    return this.quizService.createQuiz(createQuizDto, requester);
  }

  @GetMapping("/{quizId}")
  public QuizDto getQuiz(@PathVariable Long quizId) {
    User requester = AuthContext.getCurrentUser();
    this.logger.info("Get quiz: %s for '%s'".formatted(quizId, requester.getUsername()));

    return this.quizService.getQuizDto(quizId, requester);
  }

  @PatchMapping("/{quizId}")
  public QuizDto updateQuiz(
      @PathVariable Long quizId,
      @Valid @RequestBody UpdateQuizDto updateQuizDto
  ) {
    User requester = AuthContext.getCurrentUser();
    this.logger.info("Update quiz: %s with %s for '%s'"
        .formatted(quizId, updateQuizDto, requester.getUsername()));

    return this.quizService.updateQuiz(quizId, updateQuizDto, requester);
  }

  @DeleteMapping("/{quizId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteQuiz(@PathVariable Long quizId) {
    User requester = AuthContext.getCurrentUser();
    this.logger.info(
        "Delete quiz: %s for '%s'".formatted(quizId, requester.getUsername()));

    this.quizService.deleteQuiz(quizId, requester);
  }

  @PostMapping("/{quizId}/questions")
  @ResponseStatus(HttpStatus.CREATED)
  public QuestionDto createQuestion(
      @PathVariable Long quizId,
      @Valid @RequestBody CreateQuestionDto createQuestionDto
  ) {
    User requester = AuthContext.getCurrentUser();
    this.logger.info("Create question for quiz: %s with %s for '%s'"
        .formatted(quizId, createQuestionDto, requester.getUsername()));

    return this.questionService.createQuestion(quizId, createQuestionDto, requester);
  }

  @PutMapping("/{quizId}/questions/{questionId}")
  public QuestionDto updateQuestion(
      @PathVariable Long quizId,
      @PathVariable Long questionId,
      @Valid @RequestBody CreateQuestionDto createQuestionDto
  ) {
    User requester = AuthContext.getCurrentUser();
    this.logger.info("Update question: %s for quiz: %s with %s for '%s'"
        .formatted(questionId, quizId, createQuestionDto, requester.getUsername()));

    return this.questionService.updateQuestion(quizId, questionId, createQuestionDto, requester);
  }

  @DeleteMapping("/{quizId}/questions/{questionId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteQuestion(
      @PathVariable Long quizId,
      @PathVariable Long questionId
  ) {
    User requester = AuthContext.getCurrentUser();
    this.logger.info("Delete question: %s for quiz: %s for '%s'"
        .formatted(questionId, quizId, requester.getUsername()));

    this.questionService.deleteQuestion(quizId, questionId, requester);
  }

}
