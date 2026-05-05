package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    @Transactional
    public void saveUser(User user, Long[] roleIds) {
        // 1. Проверка на дубликат логина
        if (userDao.getUserByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));


        assignRoles(user, roleIds);

        userDao.saveUser(user);
    }


    @Override
    @Transactional
    public void saveUser(User user) {
        saveUser(user, null);
    }

    @Override
    @Transactional
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    @Transactional
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    @Transactional
    public void updateUser(Long id, User updatedUser, Long[] roleIds) {
        User userToUpdate = getUserById(id);
        if (userToUpdate == null) {
            throw new RuntimeException("User not found with id: " + id);
        }


        userToUpdate.setUsername(updatedUser.getUsername());
        userToUpdate.setName(updatedUser.getName());
        userToUpdate.setEmail(updatedUser.getEmail());
        userToUpdate.setAge(updatedUser.getAge());


        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            userToUpdate.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }


        assignRoles(userToUpdate, roleIds);

        userDao.updateUser(id, userToUpdate);
    }


    @Override
    @Transactional
    public void updateUser(Long id, User updatedUser) {
        updateUser(id, updatedUser, null);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userDao.getUserById(id);
        if (user != null) {
            userDao.deleteUser(user);
        }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    @Override
    @Transactional
    public List<Role> getAllRoles() {
        return userDao.getAllRoles();
    }


    private void assignRoles(User user, Long[] roleIds) {
        user.setRoles(new HashSet<>());
        if (roleIds != null && roleIds.length > 0) {
            for (Long roleId : roleIds) {
                Role role = userDao.getRoleById(roleId);
                if (role != null) {
                    user.getRoles().add(role);
                }
            }
        }
    }
}