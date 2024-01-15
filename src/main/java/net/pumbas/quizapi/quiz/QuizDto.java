package net.pumbas.quizapi.quiz;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.pumbas.quizapi.question.QuestionDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {
  private Long id;
  private String ownerId;
  private String title;
  private Boolean isPublic;
  private Set<QuestionDto> questions;
}
