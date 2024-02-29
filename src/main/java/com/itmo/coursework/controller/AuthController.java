package com.itmo.coursework.controller;

import com.itmo.coursework.dto.UserLoginDTO;
import com.itmo.coursework.model.Role;
import com.itmo.coursework.model.User;
import com.itmo.coursework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backend/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<UserLoginDTO> registerNewUser(@RequestBody User user, @RequestParam List<Long> roles) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        User u = userService.addUser(user, roles);

        UserLoginDTO result = UserLoginDTO.builder()
                .id(u.getId())
                .username(u.getUsername())
                .passwordHash(u.getPasswordHash())
                .roles(userService.getRolesForUser(u.getUsername()))
                .build();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginDTO> login(@RequestBody User user) {
        User u = userService.getByLogin(user.getUsername());
        if (u == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserLoginDTO result = UserLoginDTO.builder()
                .id(u.getId())
                .username(u.getUsername())
                .passwordHash(u.getPasswordHash())
                .roles(userService.getRolesForUser(u.getUsername()))
                .build();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles(@RequestParam String username) {
        return ResponseEntity.ok(userService.getRolesForUser(username));
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }
}
