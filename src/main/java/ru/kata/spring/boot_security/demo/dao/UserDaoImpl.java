package ru.kata.spring.boot_security.demo.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserDaoImpl() {

    }

    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public User findUserById(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User findUserByUsername(String username) {
        String query = "SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username";
        return entityManager.createQuery(query, User.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public void deleteUser(User user) {
        entityManager.remove(user);
    }

    @Override
    public void updateUser(User user, User userToUpdate) {
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setRoles(user.getRoles());
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class)
                .getResultList();
    }
}
