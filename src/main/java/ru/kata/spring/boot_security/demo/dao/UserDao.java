package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;


public interface UserDao {
    List<User> getAllUsers();
    void saveUser(User user);
    User getUserById(Long id);
    User getUserByUsername(String username);
    void updateUser(Long id, User updatedUser);
    void deleteUser(User user);
    List<Role> getAllRoles();
    Role getRoleById(Long id);
}