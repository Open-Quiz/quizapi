package net.pumbas.quizapi.question;

import net.pumbas.quizapi.exception.NotFoundException;
import net.pumbas.quizapi.quiz.Quiz;
import net.pumbas.quizapi.quiz.QuizMapper;
import net.pumbas.quizapi.quiz.QuizService;
import net.pumbas.quizapi.user.User;
import net.pumbas.quizapi.user.UserRepository;
import net.pumbas.quizapi.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

  private final QuestionRepository questionRepository;
  private final UserRepository userRepository;
  private final QuizService quizService;
  private final QuizMapper quizMapper;

  @Autowired
  public QuestionService(
      QuestionRepository questionRepository,
      UserRepository userRepository,
      QuizService quizService,
      QuizMapper quizMapper
  ) {
    this.questionRepository = questionRepository;
    this.userRepository = userRepository;
    this.quizService = quizService;
    this.quizMapper = quizMapper;
  }

  public Question getQuestion(Long questionId) {
    return this.questionRepository.findById(questionId)
        .orElseThrow(() -> new NotFoundException("Could not find question with id: " + questionId));

  }

  private Question createNewQuestion(Long quizId, CreateQuestionDto createQuestionDto) {
    Quiz quiz = this.quizService.getQuiz(quizId);
    User creator = this.userRepository.getReferenceById(UserService.TEST_USER.getId());

    return this.quizMapper.questionFromCreateQuestionDto(
        quiz, createQuestionDto, creator
    );
  }

  public QuestionDto createQuestion(Long quizId, CreateQuestionDto createQuestionDto) {
    Question newQuestion = this.createNewQuestion(quizId, createQuestionDto);
    Question createdQuestion = this.questionRepository.save(newQuestion);

    return this.quizMapper.questionDtoFromQuestion(createdQuestion);
  }

  public QuestionDto updateQuestion(
      Long quizId, Long questionId, CreateQuestionDto createQuestionDto
  ) {
    Question newQuestion = this.createNewQuestion(quizId, createQuestionDto);
    this.getQuestion(questionId);
    newQuestion.setId(questionId);
    Question createdQuestion = this.questionRepository.save(newQuestion);

    return this.quizMapper.questionDtoFromQuestion(createdQuestion);
  }

  public void deleteQuestion(Long quizId, Long questionId) {
    this.quizService.getQuiz(quizId);
    Question question = this.getQuestion(questionId);

    this.questionRepository.delete(question);
  }

}
