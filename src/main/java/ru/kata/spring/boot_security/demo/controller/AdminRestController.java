package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.DataClass;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/admin/rest")
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;
    private List<User> users;

    @Autowired
    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/get_all_data")
    public ResponseEntity<DataClass> getAllData(@AuthenticationPrincipal User user) {

        Set<Role> userRoles = user.getRoles();
        List<String> rolesToString = userRoles.stream()
                .map(Role::getAuthority)
                .collect(Collectors.toList());
        List<User> users = userService.getAllUsers();
        Set<Role> roles = roleService.getAllRoles();
        DataClass dataClass = new DataClass(user, rolesToString, users, roles);
        System.out.println("Выполнен запрос данных: " + dataClass);
        return ResponseEntity.ok(dataClass);
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
