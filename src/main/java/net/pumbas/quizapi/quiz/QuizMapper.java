package net.pumbas.quizapi.quiz;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.pumbas.quizapi.question.CreateQuestionDto;
import net.pumbas.quizapi.question.Question;
import net.pumbas.quizapi.question.QuestionDto;
import net.pumbas.quizapi.user.User;
import net.pumbas.quizapi.user.UserMapper;
import net.pumbas.quizapi.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class QuizMapper {

  private final UserMapper userMapper;

  public QuizMapper(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  public Quiz quizFromCreateQuizDto(CreateQuizDto createQuizDto) {
    Quiz quiz = Quiz.builder()
        .creator(User.builder().id(UserService.TEST_USER.getId()).build())
        .title(createQuizDto.getTitle())
        .isPublic(createQuizDto.getIsPublic())
        .build();

    Set<Question> questions = createQuizDto.getQuestions()
        .stream()
        .map(createQuestionDto -> this.questionFromCreateQuestionDto(quiz, createQuestionDto))
        .collect(Collectors.toSet());

    quiz.setQuestions(questions);
    return quiz;
  }

  public Question questionFromCreateQuestionDto(Quiz quiz, CreateQuestionDto createQuestionDto) {
    // TODO: Verify that the correct option index is within the bounds of the options list

    return Question.builder()
        .creator(User.builder().id(UserService.TEST_USER.getId()).build())
        .question(createQuestionDto.getQuestion())
        .correctOptionIndex(createQuestionDto.getCorrectOptionIndex())
        .options(new HashSet<>(createQuestionDto.getOptions()))
        .quiz(quiz)
        .build();
  }

  public QuizDto quizDtoFromQuiz(Quiz quiz) {
    return QuizDto.builder()
        .id(quiz.getId())
        .creator(this.userMapper.userDtoFromUser(quiz.getCreator()))
        .title(quiz.getTitle())
        .isPublic(quiz.getIsPublic())
        .createdAt(quiz.getCreatedAt())
        .updatedAt(quiz.getUpdatedAt())
        .questions(quiz.getQuestions().stream()
            .map(this::questionDtoFromQuestion)
            .collect(Collectors.toSet()))
        .build();
  }

  public QuestionDto questionDtoFromQuestion(Question question) {
    return QuestionDto.builder()
        .id(question.getId())
        .creator(this.userMapper.userDtoFromUser(question.getCreator()))
        .question(question.getQuestion())
        .correctOptionIndex(question.getCorrectOptionIndex())
        .createdAt(question.getCreatedAt())
        .updatedAt(question.getUpdatedAt())
        .options(question.getOptions())
        .build();
  }
}
