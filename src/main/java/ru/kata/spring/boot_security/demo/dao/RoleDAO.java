package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleDAO {
    List<Role> findAll();
    Role findById(Long id);
    Role findByName(String name);
    void save(Role role);
    void delete(Long id);
}
