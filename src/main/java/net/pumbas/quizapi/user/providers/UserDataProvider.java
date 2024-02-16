package net.pumbas.quizapi.user.providers;

public interface UserDataProvider {

  Provider getName();

  UserData extractUserData(String token);

  enum Provider {
    GOOGLE,
  }

}
