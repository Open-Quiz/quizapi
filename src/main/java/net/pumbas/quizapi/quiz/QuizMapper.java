package net.pumbas.quizapi.quiz;

import java.util.Set;
import java.util.stream.Collectors;
import net.pumbas.quizapi.option.Option;
import net.pumbas.quizapi.option.OptionDto;
import net.pumbas.quizapi.question.CreateQuestionDto;
import net.pumbas.quizapi.question.Question;
import net.pumbas.quizapi.question.QuestionDto;
import net.pumbas.quizapi.user.User;
import net.pumbas.quizapi.user.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class QuizMapper {

  private final UserMapper userMapper;

  public QuizMapper(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  public Quiz quizFromCreateQuizDto(CreateQuizDto createQuizDto, User creator) {
    Quiz quiz = Quiz.builder()
        .creator(creator)
        .title(createQuizDto.getTitle())
        .isPublic(createQuizDto.getIsPublic())
        .build();

    Set<Question> questions = createQuizDto.getQuestions()
        .stream()
        .map(createQuestionDto -> this.questionFromCreateQuestionDto(quiz, createQuestionDto,
            creator))
        .collect(Collectors.toSet());

    quiz.setQuestions(questions);
    return quiz;
  }

  public Question questionFromCreateQuestionDto(
      Quiz quiz, CreateQuestionDto createQuestionDto, User creator
  ) {

    return Question.builder()
        .creator(creator)
        .question(createQuestionDto.getQuestion())
        .imageUrl(createQuestionDto.getImageUrl())
        .options(createQuestionDto.getOptions()
            .stream()
            .map(this::optionFromOptionDto)
            .collect(Collectors.toSet()))
        .quiz(quiz)
        .build();
  }

  public Option optionFromOptionDto(OptionDto optionDto) {
    return Option.builder()
        .option(optionDto.getOption())
        .isCorrect(optionDto.getIsCorrect())
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

  public QuizSummaryDto quizSummaryDtoFromQuiz(Quiz quiz) {
    return QuizSummaryDto.builder()
        .id(quiz.getId())
        .creator(this.userMapper.userDtoFromUser(quiz.getCreator()))
        .title(quiz.getTitle())
        .isPublic(quiz.getIsPublic())
        .createdAt(quiz.getCreatedAt())
        .updatedAt(quiz.getUpdatedAt())
        .questionCount(quiz.getQuestions().size())
        .build();
  }

  public QuestionDto questionDtoFromQuestion(Question question) {
    return QuestionDto.builder()
        .id(question.getId())
        .creator(this.userMapper.userDtoFromUser(question.getCreator()))
        .question(question.getQuestion())
        .imageUrl(question.getImageUrl())
        .createdAt(question.getCreatedAt())
        .updatedAt(question.getUpdatedAt())
        .options(question.getOptions()
            .stream()
            .map(this::optionDtoFromOption)
            .collect(Collectors.toSet()))
        .build();
  }

  public OptionDto optionDtoFromOption(Option option) {
    return OptionDto.builder()
        .option(option.getOption())
        .isCorrect(option.getIsCorrect())
        .build();
  }
}
