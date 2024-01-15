package net.pumbas.quizapi.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class CreateQuestionDto {

  @NotBlank(message = "Question is mandatory")
  private String question;

  @PositiveOrZero(message = "Correct option index must be positive or zero")
  private Integer correctOptionIndex;

  @Size(min = 1, message="A question must have at least one option")
  private Set<String> options;
}
