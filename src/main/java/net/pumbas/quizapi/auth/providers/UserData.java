package net.pumbas.quizapi.auth.providers;

import lombok.Data;

@Data
public class UserData {

  /**
   * The username of the user in the provider's system.
   */
  private final String username;

  /**
   * The URL of the user's profile picture in the provider's system.
   */
  private final String pictureUrl;

  /**
   * The id of the user in the provider's system.
   */
  private final String providerId;

}
