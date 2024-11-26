package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class UserController {

	@GetMapping("/user")
	public String getProfile(@AuthenticationPrincipal User user, Model model) {
		model.addAttribute("user", user);
		Set<Role> userRoles = user.getRoles();
		List<String> rolesToString = userRoles.stream()
				.map(Role::getAuthority)
				.collect(Collectors.toList());
		model.addAttribute("rolesToString", rolesToString);
		return "user";
	}
}