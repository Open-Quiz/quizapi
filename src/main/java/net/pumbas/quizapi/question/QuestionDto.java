package net.pumbas.quizapi.question;

import java.time.ZonedDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import net.pumbas.quizapi.user.UserDto;

@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class QuestionDto extends CreateQuestionDto {

  private Long id;
  private UserDto creator;
  private ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;

}
