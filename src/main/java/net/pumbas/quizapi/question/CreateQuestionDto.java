package net.pumbas.quizapi.question;

import java.util.Set;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CreateQuestionDto {

  private String question;
  private Integer correctOptionIndex;
  private Set<String> options;
}
