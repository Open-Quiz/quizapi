package net.pumbas.quizapi.question;

import java.util.List;
import lombok.Data;

@Data
public class CreateQuestionDto {

  private String question;
  private Integer correctOptionIndex;
  private List<String> options;
}
