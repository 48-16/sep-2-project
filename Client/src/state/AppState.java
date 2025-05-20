package state;

import dtos.user.UserDataDto;

public class AppState {
    private static UserDataDto currentUser;

    public static UserDataDto getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserDataDto user) {
        currentUser = user;
    }

    public static boolean isAuthenticated() {
        return currentUser != null;
    }

    public static void logout() {
        currentUser = null;
    }

    public static UserDataDto getLoggedInUser() {
        return currentUser;
    }
}
