package com.itmo.coursework.repository;

import com.itmo.coursework.model.User;
import com.itmo.coursework.model.association.UserRole;
import com.itmo.coursework.model.association.id.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    Iterable<UserRole> findUserRoleByUser(User user);
}
