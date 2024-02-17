package net.pumbas.quizapi.quiz;

import java.time.ZonedDateTime;
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
  private ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;
  private int questionCount;

}
