package ru.kata.spring.boot_security.demo.configs;

import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {

        if (userDao.getAllRoles().isEmpty()) {
            userDao.saveUser(new Role("ADMIN"));
            userDao.saveUser(new Role("USER"));
        }

        if (userDao.getUserByUsername("admin") == null) {
            List<Role> roles = userDao.getAllRoles();
            Role adminRole = roles.stream()
                    .filter(r -> r.getName().equals("ADMIN"))
                    .findFirst()
                    .orElse(null);

            if (adminRole != null) {
                User admin = new User(
                        "admin",
                        passwordEncoder.encode("admin"),
                        "Admin",
                        "admin@test.com",
                        30
                );
                admin.setRoles(new HashSet<>(java.util.Collections.singletonList(adminRole)));
                userDao.saveUser(admin);
            }
        }

        if (userDao.getUserByUsername("user") == null) {
            List<Role> roles = userDao.getAllRoles();
            Role userRole = roles.stream()
                    .filter(r -> r.getName().equals("USER"))
                    .findFirst()
                    .orElse(null);

            if (userRole != null) {
                User user = new User(
                        "user",
                        passwordEncoder.encode("user"),
                        "User",
                        "user@test.com",
                        25
                );
                user.setRoles(new HashSet<>(java.util.Collections.singletonList(userRole)));
                userDao.saveUser(user);
            }
        }
    }
}