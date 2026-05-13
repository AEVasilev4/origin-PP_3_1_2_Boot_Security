package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();
    User getUserById(Long id);
    void saveUser(User user, Set<Role> roles);
    void updateUser(User user, Set<Role> roles);
    void deleteUser(Long id);
    List<Role> getAllRoles();
    User findByUsername(String username);
    Set<Role> getRolesByIds(List<Long> ids);

}