package net.pumbas.quizapi.user.providers;

public interface UserDataProvider {

  UserData extractUserData(String token);

}
