package net.pumbas.quizapi.quiz;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import net.pumbas.quizapi.question.CreateQuestionDto;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CreateQuizDto extends UpdateQuizDto {

  @Valid
  private List<CreateQuestionDto> questions = new ArrayList<>();

}
