package services.auth;

import dtos.auth.UserDataDto;

import java.time.LocalDateTime;
import java.util.UUID;

public class Session {
  private final String sessionId;
  private final UserDataDto user;
  private LocalDateTime lastActivity;
  private static final int SESSION_TIMEOUT_MINUTES = 30;

  public Session(UserDataDto user) {
    this.sessionId = UUID.randomUUID().toString();
    this.user = user;
    this.lastActivity = LocalDateTime.now();
  }

  public String getSessionId() {
    return sessionId;
  }

  public UserDataDto getUser() {
    return user;
  }

  public LocalDateTime getLastActivity() {
    return lastActivity;
  }

  public void updateActivity() {
    this.lastActivity = LocalDateTime.now();
  }

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(lastActivity.plusMinutes(SESSION_TIMEOUT_MINUTES));
  }
}