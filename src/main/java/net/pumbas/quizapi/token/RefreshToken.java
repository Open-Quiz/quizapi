package net.pumbas.quizapi.token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.pumbas.quizapi.user.User;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

  /**
   * The id of the refresh token.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * The current state of the refresh token. This is used to determine if the token is still valid
   * and to detect refresh token reuse.
   */
  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private RefreshTokenState state;

  /**
   * The user that the refresh token is associated with.
   */
  @ManyToOne(fetch = FetchType.EAGER)
  private User user;

  /**
   * The original refresh token that this token was created from. When refresh token reuse is
   * detected, all tokens that were created from the same original token are invalidated and cannot
   * be used.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  private RefreshToken originalToken;

  /**
   * All the refresh tokens that were created from this original token.
   */
  @OneToMany(mappedBy = "originalToken", fetch = FetchType.LAZY)
  private Set<RefreshToken> childTokens;

}
