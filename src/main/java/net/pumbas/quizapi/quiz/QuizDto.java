package net.pumbas.quizapi.quiz;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.pumbas.quizapi.question.QuestionDto;
import net.pumbas.quizapi.user.User;
import net.pumbas.quizapi.user.UserDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {
  private Long id;
  private UserDto creator;
  private String title;
  private Boolean isPublic;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Set<QuestionDto> questions;
}
