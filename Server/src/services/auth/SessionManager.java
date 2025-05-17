package services.auth;

import dtos.auth.UserDataDto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionManager {
  private static final SessionManager INSTANCE = new SessionManager();
  private final Map<String, Session> activeSessions = new ConcurrentHashMap<>();
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  private SessionManager() {
    // Schedule a task to cleanup expired sessions every 5 minutes
    scheduler.scheduleAtFixedRate(this::cleanupExpiredSessions, 5, 5, TimeUnit.MINUTES);
  }

  public static SessionManager getInstance() {
    return INSTANCE;
  }

  public Session createSession(UserDataDto user) {
    Session session = new Session(user);
    activeSessions.put(session.getSessionId(), session);
    return session;
  }

  public Session getSession(String sessionId) {
    Session session = activeSessions.get(sessionId);
    if (session != null) {
      if (session.isExpired()) {
        invalidateSession(sessionId);
        return null;
      }
      session.updateActivity();
    }
    return session;
  }

  public void invalidateSession(String sessionId) {
    activeSessions.remove(sessionId);
  }

  private void cleanupExpiredSessions() {
    activeSessions.entrySet().removeIf(entry -> entry.getValue().isExpired());
  }

  public void shutdown() {
    scheduler.shutdown();
  }
}