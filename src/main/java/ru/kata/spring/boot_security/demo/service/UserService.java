package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();
    void saveUser(User user);
    void saveUser(User user, Long[] roleIds);
    User getUserById(Long id);
    User getUserByUsername(String username);
    void updateUser(Long id, User updatedUser);
    void updateUser(Long id, User updatedUser, Long[] roleIds); // Новый метод
    void deleteUser(Long id);
    List<Role> getAllRoles();
}