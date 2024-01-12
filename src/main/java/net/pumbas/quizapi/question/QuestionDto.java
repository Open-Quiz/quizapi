package net.pumbas.quizapi.question;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class QuestionDto extends CreateQuestionDto {
  private Long id;
  private String creatorId;
}
