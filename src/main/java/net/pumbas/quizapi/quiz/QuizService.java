package net.pumbas.quizapi.quiz;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

  private final QuizRepository quizRepository;
  private final QuizMapper quizMapper;

  @Autowired
  public QuizService(QuizRepository quizRepository, QuizMapper quizMapper) {
    this.quizRepository = quizRepository;
    this.quizMapper = quizMapper;
  }

  public List<Quiz> getQuizzes() {
    return this.quizRepository.findAll();
  }

  public Quiz createQuiz(CreateQuizDto createQuizDto) {
    Quiz newQuiz = this.quizMapper.quizFromCreateQuizDto(createQuizDto);
    return this.quizRepository.save(newQuiz);
  }
}
