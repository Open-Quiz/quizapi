package net.pumbas.quizapi.option;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class OptionDto {
  private String option;
  private Boolean isCorrect;
}
