package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/all_users")
    public String getAllUsers(
            @AuthenticationPrincipal User user,
            ModelMap model
    ) {
        List<User> users = userService.getAllUsers();
        Set<Role> roles = roleService.getAllRoles();
        Set<Role> userRoles = user.getRoles();
        List<String> rolesToString = userRoles.stream()
                .map(Role::getAuthority)
                .collect(Collectors.toList());
        model.addAttribute("users", users);
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("rolesToString", rolesToString);
        model.addAttribute("showEditModal", false);
        model.addAttribute("new_user", new User());
        return "all_users";
    }
}
