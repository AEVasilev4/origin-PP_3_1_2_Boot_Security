package ru.kata.spring.boot_security.demo.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.HashSet;


@Component
public class DataInitializer implements CommandLineRunner {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    public DataInitializer(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Проверяем, есть ли уже админ. Если нет → создаём тестовых пользователей
        if (userDao.getUserByUsername("admin") == null) {

            // 1. Создаём и СРАЗУ сохраняем роли в БД (чтобы у них появился ID)
            Role roleUser = new Role();
            roleUser.setName("ROLE_USER");
            entityManager.persist(roleUser);

            Role roleAdmin = new Role();
            roleAdmin.setName("ROLE_ADMIN");
            entityManager.persist(roleAdmin);

            // 2. Создаём админа
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setName("Admin User");
            admin.setEmail("admin@test.com");
            admin.setAge(30);
            admin.setRoles(new HashSet<>()); // Инициализируем Set, чтобы не было NullPointerException
            admin.getRoles().add(roleAdmin);

            // 3. Создаём обычного пользователя
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setName("Regular User");
            user.setEmail("user@test.com");
            user.setAge(25);
            user.setRoles(new HashSet<>());
            user.getRoles().add(roleUser);

            // 4. Сохраняем пользователей. Hibernate сам создаст связи в users_roles
            userDao.saveUser(admin);
            userDao.saveUser(user);

            System.out.println("✅ Тестовые пользователи успешно созданы в базе!");
        }
    }
}