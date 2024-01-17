package net.pumbas.quizapi.question;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class QuestionDto extends CreateQuestionDto {
  private Long id;
  private String creatorId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
