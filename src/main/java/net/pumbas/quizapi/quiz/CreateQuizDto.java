package net.pumbas.quizapi.quiz;

import java.util.List;
import lombok.Data;
import net.pumbas.quizapi.question.CreateQuestionDto;

@Data
public class CreateQuizDto {

  private String title;
  private Boolean isPublic;
  private List<CreateQuestionDto> questions;
}
