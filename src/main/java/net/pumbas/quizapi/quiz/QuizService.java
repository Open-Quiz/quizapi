package net.pumbas.quizapi.quiz;

import java.util.List;
import java.util.Optional;
import net.pumbas.quizapi.user.User;
import net.pumbas.quizapi.user.UserRepository;
import net.pumbas.quizapi.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

  private final QuizRepository quizRepository;
  private final UserRepository userRepository;
  private final QuizMapper quizMapper;

  @Autowired
  public QuizService(QuizRepository quizRepository, UserRepository userRepository,
      QuizMapper quizMapper) {
    this.quizRepository = quizRepository;
    this.userRepository = userRepository;
    this.quizMapper = quizMapper;
  }

  public List<QuizDto> getQuizzes() {
    return this.quizRepository.findAll()
        .stream()
        .map(this.quizMapper::quizDtoFromQuiz)
        .toList();
  }

  public Optional<QuizDto> getQuiz(Long quizId) {
    return this.quizRepository.findById(quizId)
        .map(this.quizMapper::quizDtoFromQuiz);
  }

  public QuizDto createQuiz(CreateQuizDto createQuizDto) {
    User creator = this.userRepository.getReferenceById(UserService.TEST_USER.getId());
    Quiz newQuiz = this.quizMapper.quizFromCreateQuizDto(createQuizDto, creator);
    Quiz createdQuiz = this.quizRepository.save(newQuiz);
    return this.quizMapper.quizDtoFromQuiz(createdQuiz);
  }
}
