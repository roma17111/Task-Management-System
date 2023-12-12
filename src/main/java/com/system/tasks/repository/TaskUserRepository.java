package com.system.tasks.repository;

import com.system.tasks.entity.TaskUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskUserRepository extends JpaRepository<TaskUser,Long> {
}
