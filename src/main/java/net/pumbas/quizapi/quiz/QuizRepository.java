package net.pumbas.quizapi.quiz;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

  @Query("SELECT SIZE(q.questions) FROM Quiz q WHERE q.id = ?1")
  int countQuestionsById(Long quizId);

  @Query("SELECT q FROM Quiz q WHERE q.isPublic OR q.creator.id = ?1")
  List<Quiz> findAllViewableQuizzes(Long requesterId);

}
