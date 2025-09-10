package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;


import java.util.List;

public interface UserDAO {
    public List<User> findAll();
    public User findById(Long id);
    public void save(User user);
    public void delete(Long id);
    public User findByUsername(String username);
}
