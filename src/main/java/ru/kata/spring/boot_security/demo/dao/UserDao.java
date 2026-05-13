package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;
import java.util.Set;

public interface UserDao {
    List<User> getAllUsers();
    User getUserById(Long id);
    User getUserByUsername(String username);
    void saveUser(User user);
    void saveUser(Role role);
    void updateUser(User user);
    void deleteUser(User user);
    List<Role> getAllRoles();
    Set<Role> getRolesByIds(List<Long> ids);
}