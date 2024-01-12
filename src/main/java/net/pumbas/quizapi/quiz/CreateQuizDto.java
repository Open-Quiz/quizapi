package net.pumbas.quizapi.quiz;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.pumbas.quizapi.question.CreateQuestionDto;

@Data
@NoArgsConstructor
public class CreateQuizDto {

  private String title;
  private Boolean isPublic;
  private List<CreateQuestionDto> questions;
}
