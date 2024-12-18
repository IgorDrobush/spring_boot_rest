package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.DataClass;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.model.UsersResponse;
import ru.kata.spring.boot_security.demo.service.HandleRequestSaveAndUpdateUser;
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
    private final HandleRequestSaveAndUpdateUser handleRequestSaveAndUpdateUser;

    @Autowired
    public AdminRestController(
            UserService userService,
            RoleService roleService,
            HandleRequestSaveAndUpdateUser handleRequestSaveAndUpdateUser
    ) {
        this.userService = userService;
        this.roleService = roleService;
        this.handleRequestSaveAndUpdateUser = handleRequestSaveAndUpdateUser;
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
        return ResponseEntity.ok(dataClass);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<UsersResponse> saveUser(@RequestBody User user) {
        return ResponseEntity.ok(handleRequestSaveAndUpdateUser.handleRequest(user, "save"));
    }

    @GetMapping(value = "/delete")
    public ResponseEntity<List<User>> deleteUser(
            @RequestParam(value = "id", required = false) Long id
    ) {
        userService.deleteUserById(id);
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<UsersResponse> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(handleRequestSaveAndUpdateUser.handleRequest(user, "update"));
    }
}
