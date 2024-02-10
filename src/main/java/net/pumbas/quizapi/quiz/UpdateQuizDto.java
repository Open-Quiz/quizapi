package net.pumbas.quizapi.quiz;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuizDto {

  @NotBlank(message = "Title is mandatory")
  private String title;

  private Boolean isPublic = false;
}
