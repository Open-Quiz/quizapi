package net.pumbas.quizapi.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.pumbas.quizapi.option.OptionDto;

@Data
@SuperBuilder
@NoArgsConstructor
public class CreateQuestionDto {

  @NotBlank(message = "Question is mandatory")
  private String question;

  private String imageUrl;

  @Size(min = 1, message="A question must have at least one option")
  private Set<OptionDto> options;
}
