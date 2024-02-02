package net.pumbas.quizapi.quiz;

import jakarta.validation.Valid;
import java.util.List;
import net.pumbas.quizapi.config.Constants;
import net.pumbas.quizapi.question.CreateQuestionDto;
import net.pumbas.quizapi.question.QuestionDto;
import net.pumbas.quizapi.question.QuestionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
  public List<QuizDto> getQuizzes() {
    return this.quizService.getQuizzes();
  }


  @PostMapping
  public QuizDto createQuiz(@Valid @RequestBody CreateQuizDto createQuizDto) {
    return this.quizService.createQuiz(createQuizDto);
  }

  @GetMapping("/{quizId}")
  public QuizDto getQuiz(@PathVariable Long quizId) {
    return this.quizService.getQuizDto(quizId);
  }

  @PostMapping("/{quizId}/questions")
  public QuestionDto createQuestion(
      @PathVariable Long quizId,
      @Valid @RequestBody CreateQuestionDto createQuestionDto
  ) {
    return this.questionService.createQuestion(quizId, createQuestionDto);
  }
}
