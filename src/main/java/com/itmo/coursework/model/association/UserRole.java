package com.itmo.coursework.model.association;

import com.itmo.coursework.model.Role;
import com.itmo.coursework.model.User;
import com.itmo.coursework.model.association.id.UserRoleId;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_role")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserRole {

    @EmbeddedId
    private UserRoleId userRoleId = new UserRoleId();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
}
