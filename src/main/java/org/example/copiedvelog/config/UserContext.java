package org.example.copiedvelog.config;

import org.example.copiedvelog.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class UserContext {
    private static final ThreadLocal<User> USER_THREAD_LOCAL = ThreadLocal.withInitial(() -> null);

    public static void setUser(User user) {
        USER_THREAD_LOCAL.set(user);
    }
    public static User getUser() {
        return USER_THREAD_LOCAL.get();
    }

    public static void clear() {
        USER_THREAD_LOCAL.remove();
    }
}