package net.pumbas.quizapi.quiz;

import java.util.Set;
import lombok.Builder;
import lombok.Data;
import net.pumbas.quizapi.question.QuestionDto;

@Data
@Builder
public class QuizDto {
  private Long id;
  private String ownerId;
  private String title;
  private boolean isPublic;
  private Set<QuestionDto> questions;
}
