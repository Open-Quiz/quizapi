package net.pumbas.quizapi.quiz;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.pumbas.quizapi.user.UserDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizSummaryDto {

  private Long id;
  private UserDto creator;
  private String title;
  private Boolean isPublic;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private int questionCount;
}
