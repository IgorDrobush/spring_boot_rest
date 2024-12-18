package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.model.UsersResponse;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Service
public class HandleRequestSaveAndUpdateUser {

    private final UserService userService;

    @Autowired
    public HandleRequestSaveAndUpdateUser(UserService userService) {
        this.userService = userService;
    }

    public UsersResponse handleRequest(User user, String method) {
        try {
            if (method.equals("save")) {
                userService.saveUser(user);
            } else {
                userService.updateUser(user);
            }

            List<User> users = userService.getAllUsers();
            return new UsersResponse(users, null);
        } catch (ConstraintViolationException e) {
            StringBuilder stringBuilder = new StringBuilder();
            e.getConstraintViolations().forEach(constraintViolation -> {
                stringBuilder.append(constraintViolation.getMessage());
            });
            String error = stringBuilder.toString();
            return new UsersResponse(null, error);
        } catch (Exception e) {
            String error = e.getMessage();
            return  new UsersResponse(null, error);
        }
    }
}
