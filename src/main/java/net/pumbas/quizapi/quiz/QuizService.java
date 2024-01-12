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

  public List<QuizDto> getQuizzes() {
    return this.quizRepository.findAll()
        .stream()
        .map(this.quizMapper::quizDtoFromQuiz)
        .toList();
  }

  public QuizDto createQuiz(CreateQuizDto createQuizDto) {
    Quiz newQuiz = this.quizMapper.quizFromCreateQuizDto(createQuizDto);
    Quiz createdQuiz = this.quizRepository.save(newQuiz);
    return this.quizMapper.quizDtoFromQuiz(createdQuiz);
  }
}
