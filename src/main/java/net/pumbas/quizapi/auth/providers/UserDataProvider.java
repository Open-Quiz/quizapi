package net.pumbas.quizapi.auth.providers;

public interface UserDataProvider {

  UserData extractUserData(String token);

}
