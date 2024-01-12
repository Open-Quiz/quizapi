package net.pumbas.quizapi.question;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.pumbas.quizapi.option.Option;
import net.pumbas.quizapi.quiz.Quiz;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

  @Fetch(FetchMode.SUBSELECT)
  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<Option> options;


  @ManyToOne
  @EqualsAndHashCode.Exclude
  @JoinColumn(name = "quizId", nullable = false)
  private Quiz quiz;

}
