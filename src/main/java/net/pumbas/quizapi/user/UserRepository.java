package net.pumbas.quizapi.user;

import java.util.Optional;
import net.pumbas.quizapi.user.providers.UserDataProvider.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

  @Query("SELECT u FROM User u WHERE u.provider = ?1 AND u.providerId = ?2")
  Optional<User> getUserByProvider(Provider provider, String providerId);

}
