package net.pumbas.quizapi.auth.providers;

import lombok.Data;

@Data
public class UserData {

  private final String username;
  private final String pictureUrl;
  private final String providerId;

}
