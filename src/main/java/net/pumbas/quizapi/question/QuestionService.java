package net.pumbas.quizapi.question;

import net.pumbas.quizapi.exception.NotFoundException;
import net.pumbas.quizapi.quiz.Quiz;
import net.pumbas.quizapi.quiz.QuizMapper;
import net.pumbas.quizapi.quiz.QuizService;
import net.pumbas.quizapi.user.User;
import net.pumbas.quizapi.user.UserRepository;
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

  private Question createNewQuestion(
      Long quizId, CreateQuestionDto createQuestionDto, User requester
  ) {
    Quiz quiz = this.quizService.getQuiz(quizId, requester, true);

    return this.quizMapper.questionFromCreateQuestionDto(
        quiz, createQuestionDto, requester
    );
  }

  public QuestionDto createQuestion(
      Long quizId, CreateQuestionDto createQuestionDto, User requester
  ) {
    Question newQuestion = this.createNewQuestion(quizId, createQuestionDto, requester);
    Question createdQuestion = this.questionRepository.save(newQuestion);

    return this.quizMapper.questionDtoFromQuestion(createdQuestion);
  }

  public QuestionDto updateQuestion(
      Long quizId, Long questionId, CreateQuestionDto createQuestionDto, User requester
  ) {
    Question newQuestion = this.createNewQuestion(quizId, createQuestionDto, requester);
    this.getQuestion(questionId);
    newQuestion.setId(questionId);
    Question createdQuestion = this.questionRepository.save(newQuestion);

    return this.quizMapper.questionDtoFromQuestion(createdQuestion);
  }

  public void deleteQuestion(Long quizId, Long questionId, User requester) {
    this.quizService.getQuiz(quizId, requester, true);
    Question question = this.getQuestion(questionId);

    this.questionRepository.delete(question);
  }

}
