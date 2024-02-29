package com.itmo.coursework.service;

import com.itmo.coursework.model.Role;
import com.itmo.coursework.model.User;
import com.itmo.coursework.model.association.UserRole;
import com.itmo.coursework.repository.UserRepository;
import com.itmo.coursework.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    public User addUser(User user, List<Long> roles) {
        User result = repository.save(user);
        roles.forEach(roleId -> {
            Role role = new Role();
            role.setId(roleId);
            UserRole userRole = new UserRole();
            userRole.setUser(result);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        });
        return result;
    }

    public User getByLogin(String login) {
        return repository.findByUsername(login).orElse(null);
    }

    public List<Role> getRolesForUser(String username) {
        return StreamSupport.stream(userRoleRepository.findUserRoleByUser(getByLogin(username)).spliterator(), false)
                .map(UserRole::getRole).collect(Collectors.toList());
    }

    public List<User> getAll() {
        return repository.findAll();
    }
}
