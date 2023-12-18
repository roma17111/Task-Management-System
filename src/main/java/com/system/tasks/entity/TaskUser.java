package com.system.tasks.entity;

import com.system.tasks.security.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "task_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class TaskUser {

    /**
     * Entity сущность пользователя для взаимодействия с БД
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    long id;

    @Column(name = "email", nullable = false)
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "first_name")
    String firstName;
    @Column(name = "last_name")
    String lastName;
    @Column(name = "second_name")
    String secondName;

    Set<Role> rolesSet;
}
