package net.pumbas.quizapi.option;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.pumbas.quizapi.question.Question;

@Data
@Entity
public class Option {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false)
  private String option;

  @Column(nullable = false)
  private Boolean isCorrect;

  @ManyToOne
  @EqualsAndHashCode.Exclude
  @JoinColumn(name = "questionId", nullable = false)
  private Question question;
}
