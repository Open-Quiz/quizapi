package net.pumbas.quizapi.quiz;

import java.util.List;
import net.pumbas.quizapi.config.Constants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API_V1 + "/quizzes")
public class QuizController {

  private final QuizService quizService;

  public QuizController(QuizService quizService) {
    this.quizService = quizService;
  }

  @GetMapping
  public List<QuizDto> getQuizzes() {
    return this.quizService.getQuizzes();
  }

  @PostMapping
  public QuizDto createQuiz(@RequestBody CreateQuizDto createQuizDto) {
    return this.quizService.createQuiz(createQuizDto);
  }
}
