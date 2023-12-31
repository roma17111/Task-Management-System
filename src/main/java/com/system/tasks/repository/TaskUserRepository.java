package com.system.tasks.repository;

import com.system.tasks.entity.TaskUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskUserRepository extends JpaRepository<TaskUser,Long> {

    TaskUser findByEmail(String email);
}
