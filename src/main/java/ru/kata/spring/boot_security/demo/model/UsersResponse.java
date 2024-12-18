package ru.kata.spring.boot_security.demo.model;

import java.util.List;

public class UsersResponse {

    public final List<User> users;
    public final String error;

    public UsersResponse(List<User> users, String error) {
        this.users = users;
        this.error = error;
    }
}
