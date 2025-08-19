package ru.kata.spring.boot_security.demo.configs;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Profile("!prod")
public class DataSeeder implements ApplicationRunner {

    @PersistenceContext
    private EntityManager em;

    private final PasswordEncoder encoder;

    public DataSeeder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Role roleUser  = findOrCreateRole("ROLE_USER");
        Role roleAdmin = findOrCreateRole("ROLE_ADMIN");

        if (findUserByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin"));
            admin.setName("Администратор");
            admin.setEmail("admin@example.com");
            admin.setAge(30);
            admin.setRoles(new HashSet<>(Set.of(roleAdmin, roleUser)));
            em.persist(admin);
        }

        if (findUserByUsername("user") == null) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(encoder.encode("user"));
            user.setName("Пользователь");
            user.setEmail("user@example.com");
            user.setAge(25);
            user.setRoles(new HashSet<>(Set.of(roleUser)));
            em.persist(user);
        }
    }

    private Role findOrCreateRole(String name) {
        List<Role> list = em.createQuery("select r from Role r where r.name = :n", Role.class)
                .setParameter("n", name).getResultList();
        if (!list.isEmpty()) return list.get(0);
        Role r = new Role(name);
        em.persist(r);
        return r;
    }

    private User findUserByUsername(String username) {
        List<User> list = em.createQuery("select u from User u where u.username = :un", User.class)
                .setParameter("un", username).setMaxResults(1).getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
}