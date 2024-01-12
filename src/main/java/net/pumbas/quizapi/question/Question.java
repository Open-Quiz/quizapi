package net.pumbas.quizapi.question;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.pumbas.quizapi.quiz.Quiz;

@Data
@Entity
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false)
  private String creatorId;

  @Column(nullable = false)
  private String question;

  @Column(nullable = false)
  private Integer correctOptionIndex;

  @ElementCollection
  private Set<String> options;

  @ManyToOne
  @EqualsAndHashCode.Exclude
  @JoinColumn(nullable = false)
  private Quiz quiz;

}
