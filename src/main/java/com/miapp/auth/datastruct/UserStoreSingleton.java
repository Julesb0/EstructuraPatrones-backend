package com.miapp.auth.datastruct;

import com.miapp.auth.domain.User;
import java.util.concurrent.ConcurrentHashMap;

public class UserStoreSingleton {
    private static UserStoreSingleton instance;
    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    private UserStoreSingleton() {}

    public static synchronized UserStoreSingleton getInstance() {
        if (instance == null) {
            instance = new UserStoreSingleton();
        }
        return instance;
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public void removeUser(String userId) {
        users.remove(userId);
    }

    public boolean containsUser(String userId) {
        return users.containsKey(userId);
    }
}