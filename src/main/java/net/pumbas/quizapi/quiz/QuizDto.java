package net.pumbas.quizapi.quiz;

import java.time.ZonedDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.pumbas.quizapi.question.QuestionDto;
import net.pumbas.quizapi.user.UserDto;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {

  private Long id;
  private UserDto creator;
  private String title;
  private Boolean isPublic;
  private ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;
  private Set<QuestionDto> questions;

}
