package net.pumbas.quizapi.quiz;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import net.pumbas.quizapi.question.CreateQuestionDto;
import net.pumbas.quizapi.question.Question;
import net.pumbas.quizapi.question.QuestionDto;
import org.springframework.stereotype.Service;

@Service
public class QuizMapper {
  private static final String TEST_OWNER_ID = "test_owner_id";
  public Quiz quizFromCreateQuizDto(CreateQuizDto createQuizDto) {
    Quiz quiz = Quiz.builder()
        .ownerId(TEST_OWNER_ID)
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
        .creatorId(TEST_OWNER_ID)
        .question(createQuestionDto.getQuestion())
        .correctOptionIndex(createQuestionDto.getCorrectOptionIndex())
        .options(new HashSet<>(createQuestionDto.getOptions()))
        .quiz(quiz)
        .build();
  }

  public QuizDto quizDtoFromQuiz(Quiz quiz) {
    return QuizDto.builder()
        .id(quiz.getId())
        .ownerId(quiz.getOwnerId())
        .title(quiz.getTitle())
        .isPublic(quiz.getIsPublic())
        .questions(quiz.getQuestions().stream()
            .map(this::questionDtoFromQuestion)
            .collect(Collectors.toSet()))
        .build();
  }

  public QuestionDto questionDtoFromQuestion(Question question) {
    return QuestionDto.builder()
        .id(question.getId())
        .creatorId(question.getCreatorId())
        .question(question.getQuestion())
        .correctOptionIndex(question.getCorrectOptionIndex())
        .options(question.getOptions())
        .build();
  }
}
