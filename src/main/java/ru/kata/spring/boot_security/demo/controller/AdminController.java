package ru.kata.spring.boot_security.demo.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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
    private Set<Role> roles;
    private List<String> rolesToString;
    private List<User> users;

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
        users = userService.getAllUsers();
        roles = roleService.getAllRoles();
        Set<Role> userRoles = user.getRoles();
        rolesToString = userRoles.stream()
                .map(Role::getAuthority)
                .collect(Collectors.toList());
        model.addAttribute("users", users);
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("rolesToString", rolesToString);
        model.addAttribute("showEditModal", false);
        return "all_users";
    }

    @GetMapping(value = "/save_user_form")
    public String saveUserForm(
            @AuthenticationPrincipal User user,
            ModelMap model
    ) {
        model.addAttribute("new_user", new User());
        model.addAttribute("roles", roles);
        model.addAttribute("user", user);
        model.addAttribute("rolesToString", rolesToString);
        return "save_user_form";
    }

    @PostMapping(value = "/save")
    public String saveUser(
            @Valid @ModelAttribute("new_user") User user,
            BindingResult bindingResult,
            ModelMap model
    ) {
        if (bindingResult.hasErrors()) {
            roles = roleService.getAllRoles();
            model.addAttribute("roles", roles);
            model.addAttribute("user", user);
            model.addAttribute("rolesToString", rolesToString);
            return "save_user_form";
        }

        try {
            userService.saveUser(user);
        } catch (RuntimeException e) {
            bindingResult.rejectValue(
                    "username",
                    "username.exists",
                    "Пользователь с таким именем уже существует"
            );
            roles = roleService.getAllRoles();
            model.addAttribute("user", user);
            model.addAttribute("rolesToString", rolesToString);
            model.addAttribute("roles", roles);
            return "save_user_form";
        }

        return "redirect:/admin/all_users";
    }

    @GetMapping(value = "/delete")
    public String deleteUser(@RequestParam(value = "id", required = false) Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/all_users";
    }

    @PostMapping(value = "/update")
    public String updateUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            ModelMap model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", users);
            model.addAttribute("user", user);
            model.addAttribute("roles", roles);
            model.addAttribute("rolesToString", rolesToString);
            model.addAttribute("showEditModal", true);
            return "all_users";
        }

        userService.updateUser(user);
        return "redirect:/admin/all_users";
    }
}
