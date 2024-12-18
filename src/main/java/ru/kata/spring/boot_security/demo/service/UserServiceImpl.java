package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.validation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    @Autowired
    public UserServiceImpl(
            UserDao userDao,
            RoleDao roleDao,
            PasswordEncoder passwordEncoder,
            Validator validator
    ) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Set<Role> rolesToSave = getRolesToSave(user);
        user.setRoles(rolesToSave);
        userDao.saveUser(user);
    }

    @Override
    @Transactional
    public User findUserById(long id) {
        return userDao.findUserById(id);
    }

    @Override
    @Transactional
    public void deleteUserById(long id) {
        User user = userDao.findUserById(id);
        if (user != null) {
            userDao.deleteUser(user);
        }
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        User userToUpdate = userDao.findUserById(user.getId());
        Set<Role> rolesToSave = getRolesToSave(user);
        user.setRoles(rolesToSave);
        userDao.updateUser(user, userToUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    private Set<Role> getRolesToSave(User user) {
        Set<Role> rolesToSave = new HashSet<>();
        Set<Role> usersRoles = user.getRoles();
        for (Role usersRole : usersRoles) {
            Role role = roleDao.findRoleByName(usersRole.getAuthority());
            if (role != null) {
                rolesToSave.add(role);
            }
        }

        return rolesToSave;
    }
}
