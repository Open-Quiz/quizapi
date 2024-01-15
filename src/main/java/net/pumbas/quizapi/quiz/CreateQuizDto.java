package net.pumbas.quizapi.quiz;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.pumbas.quizapi.question.CreateQuestionDto;

@Data
@NoArgsConstructor
public class CreateQuizDto {

  @NotBlank(message = "Title is mandatory")
  private String title;

  private Boolean isPublic = false;

  @Valid
  @Size(min = 1, message="A quiz must have at least one question")
  private List<CreateQuestionDto> questions;
}
