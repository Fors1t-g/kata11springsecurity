package ru.kata.spring.boot_security.demo.configs;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Set;

@Component
@Profile("!prod")
public class DataSeeder implements ApplicationRunner {

    @PersistenceContext
    private EntityManager em;

    private final UserService userService;

    public DataSeeder(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Role roleUser = findOrCreateRole("ROLE_USER");
        Role roleAdmin = findOrCreateRole("ROLE_ADMIN");

        if (userService.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setName("Администратор");
            admin.setEmail("admin@example.com");
            admin.setAge(30);
            admin.setRoles(new HashSet<>(Set.of(roleAdmin, roleUser)));
            userService.save(admin);
        }

        if (userService.findByUsername("user") == null) {
            User user = new User();
            user.setUsername("user");
            user.setPassword("user");
            user.setName("Пользователь");
            user.setEmail("user@example.com");
            user.setAge(25);
            user.setRoles(new HashSet<>(Set.of(roleUser)));
            userService.save(user);
        }
    }

    private Role findOrCreateRole(String name) {
        Role r = em.createQuery("select r from Role r where r.name = :n", Role.class)
                .setParameter("n", name)
                .getResultStream()
                .findFirst()
                .orElse(null);
        if (r != null) return r;
        r = new Role(name);
        em.persist(r);
        return r;
    }
}
