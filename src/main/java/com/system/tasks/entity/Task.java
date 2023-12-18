package com.system.tasks.entity;

import com.system.tasks.enums.TaskPriority;
import com.system.tasks.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class Task {

    /**
     * Entity сущность задачи для взаимодействия с БД
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    long id;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "description", nullable = false)
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status")
    TaskStatus taskStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_priority")
    TaskPriority taskPriority;

    @OneToOne
    TaskUser author;

    @OneToOne
    TaskUser executor;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    List<Comment> comments;

    public void addComment(Comment comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        if (!comments.contains(comment)) {
            comments.add(comment);
        }
    }

}
