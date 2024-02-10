package net.pumbas.quizapi.quiz;

import java.util.List;
import net.pumbas.quizapi.exception.NotFoundException;
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
  public QuizService(
      QuizRepository quizRepository,
      UserRepository userRepository,
      QuizMapper quizMapper
  ) {
    this.quizRepository = quizRepository;
    this.userRepository = userRepository;
    this.quizMapper = quizMapper;
  }

  public List<QuizSummaryDto> getQuizzes() {
    return this.quizRepository.findAll()
        .stream()
        .map((quiz) -> {
          int questionCount = this.quizRepository.countQuestionsById(quiz.getId());
          return this.quizMapper.quizSummaryDtoFromQuiz(quiz, questionCount);
        })
        .toList();
  }

  public Quiz getQuiz(Long quizId) {
    return this.quizRepository.findById(quizId)
        .orElseThrow(() -> new NotFoundException("Could not find quiz with id: " + quizId));
  }

  public QuizDto getQuizDto(Long quizId) {
    return this.quizMapper.quizDtoFromQuiz(this.getQuiz(quizId));
  }

  public QuizDto createQuiz(CreateQuizDto createQuizDto) {
    User creator = this.userRepository.getReferenceById(UserService.TEST_USER.getId());
    Quiz newQuiz = this.quizMapper.quizFromCreateQuizDto(createQuizDto, creator);
    Quiz createdQuiz = this.quizRepository.save(newQuiz);
    return this.quizMapper.quizDtoFromQuiz(createdQuiz);
  }

  public QuizDto updateQuiz(Long quizId, UpdateQuizDto updateQuizDto) {
    Quiz quiz = this.getQuiz(quizId);
    quiz.setTitle(updateQuizDto.getTitle());
    quiz.setIsPublic(updateQuizDto.getIsPublic());
    
    Quiz updatedQuiz = this.quizRepository.save(quiz);
    return this.quizMapper.quizDtoFromQuiz(updatedQuiz);
  }

  public void deleteQuiz(Long quizId) {
    Quiz quiz = this.getQuiz(quizId);
    this.quizRepository.delete(quiz);
  }
}
