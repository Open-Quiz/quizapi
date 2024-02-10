package net.pumbas.quizapi.quiz;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.pumbas.quizapi.question.CreateQuestionDto;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateQuizDto extends UpdateQuizDto {
  
  @Valid
  @Size(min = 1, message = "A quiz must have at least one question")
  private List<CreateQuestionDto> questions;
}
