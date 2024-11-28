package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/rest")
public class AdminRestController {

    private final UserService userService;
    private List<User> users;

    @Autowired
    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/save")
    public ResponseEntity<List<User>> saveUser(@RequestBody User user) {
        System.out.println("Сохраняется юзер: " + user);
        userService.saveUser(user);
        users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "/delete")
    public ResponseEntity<List<User>> deleteUser(
            @RequestParam(value = "id", required = false) Long id
    ) {
        userService.deleteUserById(id);
        users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<List<User>> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
