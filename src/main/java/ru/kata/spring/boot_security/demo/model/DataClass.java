package ru.kata.spring.boot_security.demo.model;

import java.util.List;
import java.util.Set;

public class DataClass {
    public final User user;
    public final List<String> rolesToString;
    public final List<User> users;
    public final Set<Role> roles;

    public DataClass(User user, List<String> rolesToString, List<User> users, Set<Role> roles) {
        this.user = user;
        this.rolesToString = rolesToString;
        this.users = users;
        this.roles = roles;
    }
}
