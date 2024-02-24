package net.pumbas.quizapi.quiz;

import java.util.List;
import net.pumbas.quizapi.exception.ForbiddenException;
import net.pumbas.quizapi.exception.NotFoundException;
import net.pumbas.quizapi.user.User;
import net.pumbas.quizapi.user.UserRepository;
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

  public List<QuizSummaryDto> getQuizzes(User requester) {
    return this.quizRepository.findAllViewableQuizzes(requester.getId())
        .stream()
        .map((quiz) -> {
          int questionCount = this.quizRepository.countQuestionsById(quiz.getId());
          return this.quizMapper.quizSummaryDtoFromQuiz(quiz, questionCount);
        })
        .toList();
  }

  public Quiz getQuiz(Long quizId, User requester, boolean checkCanEdit) {
    Quiz quiz = this.quizRepository.findById(quizId)
        .orElseThrow(() -> new NotFoundException("Could not find quiz with id: " + quizId));

    if (checkCanEdit) {
      this.validateCanEdit(quiz, requester);
    } else {
      this.validateCanView(quiz, requester);
    }

    return quiz;
  }

  public Quiz getQuiz(Long quizId, User requester) {
    return this.getQuiz(quizId, requester, false);
  }

  public QuizDto getQuizDto(Long quizId, User requester) {
    return this.quizMapper.quizDtoFromQuiz(this.getQuiz(quizId, requester));
  }

  public QuizDto createQuiz(CreateQuizDto createQuizDto, User requester) {
    Quiz newQuiz = this.quizMapper.quizFromCreateQuizDto(createQuizDto, requester);
    Quiz createdQuiz = this.quizRepository.save(newQuiz);
    return this.quizMapper.quizDtoFromQuiz(createdQuiz);
  }

  public QuizDto updateQuiz(Long quizId, UpdateQuizDto updateQuizDto, User requester) {
    Quiz quiz = this.getQuiz(quizId, requester, true);

    quiz.setTitle(updateQuizDto.getTitle());
    quiz.setIsPublic(updateQuizDto.getIsPublic());

    Quiz updatedQuiz = this.quizRepository.save(quiz);
    return this.quizMapper.quizDtoFromQuiz(updatedQuiz);
  }

  public void deleteQuiz(Long quizId, User requester) {
    Quiz quiz = this.getQuiz(quizId, requester, true);

    this.quizRepository.delete(quiz);
  }

  private void validateCanView(Quiz quiz, User requester) {
    if (quiz.getIsPublic()) {
      return;
    }

    this.validateCanEdit(quiz, requester);
  }

  private void validateCanEdit(Quiz quiz, User requester) {
    if (!quiz.getCreator().getId().equals(requester.getId())) {
      throw new ForbiddenException("The user '%s' cannot edit the quiz: %s"
          .formatted(requester.getUsername(), quiz.getId()));
    }
  }

}
