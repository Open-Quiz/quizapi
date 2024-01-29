package net.pumbas.quizapi.option;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Option {

  @Column(nullable = false)
  private String option;

  @Column(nullable = false)
  private Boolean isCorrect;
}
