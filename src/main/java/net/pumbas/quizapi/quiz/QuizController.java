package net.pumbas.quizapi.quiz;

import jakarta.validation.Valid;
import java.util.List;
import net.pumbas.quizapi.config.Constants;
import net.pumbas.quizapi.exception.NotFoundException;
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

  public QuizController(QuizService quizService) {
    this.quizService = quizService;
  }

  @GetMapping
  public List<QuizDto> getQuizzes() {
    return this.quizService.getQuizzes();
  }

  @GetMapping("/{quizId}")
  public QuizDto getQuiz(@PathVariable Long quizId) {
    return this.quizService.getQuiz(quizId)
        .orElseThrow(() -> new NotFoundException("Could not find quiz with id: " + quizId));
  }

  @PostMapping
  public QuizDto createQuiz(@Valid @RequestBody CreateQuizDto createQuizDto) {
    return this.quizService.createQuiz(createQuizDto);
  }
}
