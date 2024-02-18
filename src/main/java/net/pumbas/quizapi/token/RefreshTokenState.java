package net.pumbas.quizapi.token;

/**
 * This is used to track the state of a refresh token and whether it has been used or invalidated
 * (Due to a refresh token reuse attack).
 * <p>
 * Refresh token reuse is detected if a token with the {@link RefreshTokenState#INVALIDATED_USED}
 * state is used again.
 */
public enum RefreshTokenState {
  UNUSED,
  INVALIDATED,
  INVALIDATED_USED
}
