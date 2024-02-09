package net.pumbas.quizapi.quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

  @Query("SELECT SIZE(q.questions) FROM Quiz q WHERE q.id = ?1")
  int countQuestionsById(Long quizId);
}
