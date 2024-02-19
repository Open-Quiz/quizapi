package net.pumbas.quizapi.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  /**
   * Invalidate all refresh tokens that are part of this refresh token's family that are currently
   * {@link RefreshTokenState#UNUSED}.
   * <p>
   * The family of a refresh token is defined as all refresh tokens that were created from the same
   * original refresh token through rotation.
   *
   * @param originalId The id of the refresh token that is the root of the family
   */
  @Query("""
      UPDATE RefreshToken r SET r.state = 'INVALIDATED'
      WHERE (r.id = ?1 OR r.originalToken.id = ?1) AND r.state = 'UNUSED'
      """
  )
  void invalidateRefreshTokenFamily(Long originalId);

}
